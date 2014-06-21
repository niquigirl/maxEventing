package com.max.services.impl;

import com.max.db.model.RemoteSubscriber;
import com.max.db.repositories.RemoteSubscriberRepository;
import com.max.messaging.MaxTopic;
import com.max.messaging.TopicSettings;
import com.max.messaging.publish.InvalidMessageException;
import com.max.messaging.subscribe.DurableTopicSubscriber;
import com.max.messaging.subscribe.SubscriptionDetails;
import com.max.messaging.subscribe.TopicManagementException;
import com.max.services.InvalidSubscriberException;
import com.max.services.QueueManager;
import com.max.web.model.DefaultActivityMessage;
import com.max.web.model.RemoteSubscription;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * This class should handle high-level management of remote subscribers.
 * </p>
 * <p>
 * Upon loading, this class will query the Remote Subscribers from the DB and create
 * a {@link com.max.services.impl.RemoteSubscriberFacade} for each, and register them
 * as listeners
 * </p>
 */
public class ActivityQueueManager implements QueueManager
{
    public static final int DEFAULT_TIMEOUT = 30000;
    public static final String REMOTE_SUBSCRIBER_FACADE = "remoteSubscriberFacade";

    Logger log = Logger.getLogger(ActivityQueueManager.class);

    @Autowired
    RemoteSubscriberRepository remoteSubscriberRepository;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DurableTopicSubscriber topicSubscriber;

    Map<MaxTopic, TopicSettings> topicSettings;

    private static SubscriberCache cache = new SubscriberCache();

    /**
     * Publish a message to the Activity Topic
     *
     * @param msgObject {@link com.max.web.model.DefaultActivityMessage}
     * @throws javax.naming.NamingException
     * @throws javax.jms.JMSException
     */
    // TODO: validate message and sender prior to sending
    @Override
    public void sendMessage(MaxTopic maxTopic, String msgObject) throws NamingException, JMSException, InvalidMessageException, IOException, JSONException
    {
        TopicSettings settings = getTopicSettings().get(maxTopic);
        log.debug("Request to send Message to : " + maxTopic + " : "  + msgObject);

        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, settings.getQpidIcf());
        properties.put(settings.getConnectionFactoryNamePrefix() + settings.getConnectionFactoryName(), getTCPConnectionURL(settings));
        properties.put(settings.getTopicNamePrefix() + settings.getTopicName(), settings.getTopicName());

        InitialContext ctx = new InitialContext(properties);

        // Lookup connection factory
        TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(settings.getConnectionFactoryName());
        TopicConnection topicConnection = connFactory.createTopicConnection();
        topicConnection.start();

        TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
        // Send message
        Topic topic = (Topic) ctx.lookup(settings.getTopicName());
        // create the message to send
        TextMessage objectMessage = topicSession.createTextMessage(msgObject);

        setMessageProperties(objectMessage, DefaultActivityMessage.getInstance(msgObject));
        javax.jms.TopicPublisher topicPublisher = topicSession.createPublisher(topic);
        topicPublisher.send(objectMessage);
        System.out.println("SENT");
        topicPublisher.close();
        topicSession.close();
        topicConnection.close();
    }

    /**
     * Take all the properties defined in the
     * @param objectMessage {@code javax.jms.TextMessage} High-level message on which to set metadata
     * @param msgObject {@link com.max.web.model.DefaultActivityMessage}
     * @throws JMSException
     */
    private void setMessageProperties(TextMessage objectMessage, DefaultActivityMessage msgObject) throws JMSException
    {
        Properties metaProperties = msgObject.getMetaPropertiesForPublish();
        for (Object curProperty : metaProperties.keySet())
        {
            objectMessage.setStringProperty(curProperty.toString(), metaProperties.get(curProperty).toString());
        }
    }

    private String getTCPConnectionURL(TopicSettings settings)
    {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        final String connectionUrl = "amqp://" + settings.getUserName() + ":" + settings.getPassword() + "@" +
                settings.getCarbonClientId() + "/" + settings.getCarbonVirtualHostName() + "?brokerlist='tcp://" + settings.getCarbonDefaultHostname() + ":" + settings.getCarbonDefaultPort() + "'";
        log.info("Connecting to " + connectionUrl);
        return connectionUrl;
    }

    /**
     * From all RemoteSubscribers found in the data store, register a new RemoteSubscriberFacade
     */
    @PostConstruct
    public void registerAllManagedListeners() throws InvalidSubscriberException
    {
        getLog().info("Running post construct on RemoteSubscriberFacadeFactory: " + this);
        final Collection<RemoteSubscriber> remoteSubscribers = getRemoteSubscriberRepository().findByAutoRegister(true);

        StringBuilder errorMessage = new StringBuilder("Failures when attempting to register all listeners from the registry:");
        boolean hasErrors = false;

        for (RemoteSubscriber curSubscriber : remoteSubscribers)
        {
            try
            {
                doRegister(curSubscriber);
            }
            catch (InvalidSubscriberException | TopicManagementException e)
            {
                errorMessage.append(e.getMessage()).append(" | ");
                hasErrors = true;
            }
        }

        printStats();

        if (hasErrors)
            throw new InvalidSubscriberException(errorMessage.toString());
    }

    /**
     * Allows us to register a single remote subscriber. This method saves the subscriber to the DB and then goes through
     * and registers all subscribers
     *
     * @param subscription {@link com.max.web.model.RemoteSubscription}
     */
    public void register(@NotNull RemoteSubscription subscription) throws TopicManagementException, InvalidSubscriberException
    {
        // TODO: THIS SHOULD BE PULLED OUT INTO A SERVICE THAT UNDERSTANDS THE REQUIREMENTS OF THE DB
        RemoteSubscriber subscriber = getRemoteSubscriberRepository().findByTopicAndName(subscription.getTopic(), subscription.getName());
        if (subscriber != null)
        {
            subscriber.setAutoRegister(true);
            subscriber.setFilterString(subscription.getFilterString());
        }
        else
            subscriber = subscription.toData();

        getRemoteSubscriberRepository().save(subscriber);

        doRegister(subscriber);
    }

    /**
     * <p>
     *     Performs the following IF the subscriber name is found in the cache:
     *     <ul>
     *         <li>Unregisters subscriber from the topic to which it is subscribed</li>
     *         <li>Deactivates (sets autoRegister to false) the Remote Subscriber in the DB</li>
     *         <li>Removes the subscriber from the cache</li>
     *     </ul>
     * </p>
     *
     * @param topic {@link com.max.messaging.MaxTopic} Topic from which to unregister subscriber
     * @param subscriberName {@code String} Naming the subscriber to unregister
     * @throws TopicManagementException In the event of a failure to unregister the subscriber from the queue for any reason
     */
    @Override
    public void unregister(MaxTopic topic, @NotNull String subscriberName) throws TopicManagementException
    {
        final SubscriptionDetails subscriber = cache.getCachedSubscriber(topic, subscriberName);
        if (subscriber != null)
        {
            topicSubscriber.unregister(getTopicSettings().get(topic), subscriberName);
            deactivateSubscriberInRegistry(topic, subscriberName);

            cache.removeCachedSubscriber(topic, subscriberName);
        }
    }

    /**
     * Utility to handle "deactivating" the subscriber from the registry, meaning to set the autoRegister to false
     *
     * @param subscriberName {@code String} Naming the remote subscriber to remove from the registry
     */
    private void deactivateSubscriberInRegistry(MaxTopic topic, @NotNull final String subscriberName)
    {
        final RemoteSubscriber remoteSubscriber = getRemoteSubscriberRepository().findByTopicAndName(topic, subscriberName);
        if (remoteSubscriber != null)
        {
            remoteSubscriber.setAutoRegister(false);
            getRemoteSubscriberRepository().save(remoteSubscriber);
        }
    }

    /**
     * This method registers and caches the Subscription. If the subscription is already cached, the cached subscriber is deactivated
     * prior to registering, assuming the registry is intended to be changed
     *
     * @param curSubscriber {@link com.max.db.model.RemoteSubscriber}
     * @throws InvalidSubscriberException In the event of any validation failures
     * @throws com.max.messaging.subscribe.TopicManagementException in the event of some failure registering
     */
    private void doRegister(RemoteSubscriber curSubscriber) throws InvalidSubscriberException, TopicManagementException
    {
        validateSubscriber(curSubscriber);
        if (cache.isCached(curSubscriber.getTopic(), curSubscriber.getName()))
        {
            unregister(curSubscriber.getTopic(), curSubscriber.getName());
        }

        SubscriptionDetails details = buildSubscriptionDetails(curSubscriber);

/*
        // Don't test now; we have a 'mode' that should be
        final HandlerResults handlerResults = details.getListener().onTest(ActivityMessage.generateTestInstance());
        if (handlerResults == null || !handlerResults.isSuccess())
        {
            throw new InvalidSubscriberException("Subscriber " + curSubscriber.getName() + " Failed test");
        }

*/
        cache.cacheSubscriber(curSubscriber.getName(), details);
        topicSubscriber.register(getTopicSettings().get(curSubscriber.getTopic()), details);
    }

    /**
     * Runs through basic validation of the subscriber
     *
     * @param subscriber {@link com.max.db.model.RemoteSubscriber}
     * @throws InvalidSubscriberException
     */
    private void validateSubscriber(RemoteSubscriber subscriber) throws InvalidSubscriberException
    {
        StringBuilder errors = new StringBuilder();
        if (StringUtils.isEmpty(subscriber.getRestUrl()))
            errors.append(errors.length() > 0 ? ", " : "").append("REST URL is required ");

        if (StringUtils.isEmpty(subscriber.getName()))
            errors.append(errors.length() > 0 ? ", " : "").append("Name is required ");

        if (errors.length() > 0)
            throw new InvalidSubscriberException(errors.toString());
    }

    public void printStats()
    {
        for (SubscriptionDetails curSubscription : cache.getCachedSubscribers())
        {
            getLog().info("***** Listener " + curSubscription.getListener());
        }
    }

    /**
     * Build Subscription details from the RemoteSubscriber data
     *
     * @param curSubscriber {@link com.max.db.model.RemoteSubscriber}
     * @return {@link com.max.messaging.subscribe.SubscriptionDetails}
     */
    private SubscriptionDetails buildSubscriptionDetails(RemoteSubscriber curSubscriber)
    {
        RemoteSubscriberFacade curFacade = generateRemoteSubscriber(curSubscriber);

        SubscriptionDetails details = new SubscriptionDetails();
        details.setListener(curFacade);
        details.setFilterString(curSubscriber.getFilterString());
        details.setSubscriberName(curSubscriber.getName());
        details.setTopic(curSubscriber.getTopic());

        return details;
    }

    /**
     * Create a new {@link com.max.services.impl.RemoteSubscriberFacade} from the {@link com.max.db.model.RemoteSubscriber}
     *
     * @param curSubscriber {@link com.max.db.model.RemoteSubscriber}
     * @return {@link com.max.services.impl.RemoteSubscriberFacade}
     */
    private RemoteSubscriberFacade generateRemoteSubscriber(RemoteSubscriber curSubscriber)
    {
        RemoteSubscriberFacade curFacade = (RemoteSubscriberFacade) applicationContext.getBean(REMOTE_SUBSCRIBER_FACADE);
        curFacade.setServiceUrl(curSubscriber.getRestUrl());
        curFacade.setTimeoutMS(curSubscriber.getTimeout() == null ? DEFAULT_TIMEOUT : curSubscriber.getTimeout());
        curFacade.setName(curSubscriber.getName());

        return curFacade;
    }

    public RemoteSubscriberRepository getRemoteSubscriberRepository()
    {
        return remoteSubscriberRepository;
    }

    public Logger getLog()
    {
        return log;
    }


    /**
     * Maintain a cache of all listeners
     */
    static class SubscriberCache
    {
        private final HashMap<String, SubscriptionDetails> remoteSubscribersCache = new HashMap<>();

        /**
         * Cache a {@link com.max.messaging.subscribe.MaxMessageListener} under the subscriber name
         *
         * @param subscriberName {@code String}
         * @param details {@link com.max.messaging.subscribe.SubscriptionDetails}
         */
        public void cacheSubscriber(String subscriberName, SubscriptionDetails details)
        {
            synchronized (remoteSubscribersCache)
            {
                remoteSubscribersCache.put(composeSubscriberName(details.getTopic(), subscriberName), details);
            }
        }

        /**
         * Determine if there is a subscriber cached by the name provided
         *
         * @param subscriberName {@code String}
         * @return {@code boolean} where true indicates the subscriber name was found in the cache
         */
        public boolean isCached(MaxTopic topic, String subscriberName)
        {
            synchronized (remoteSubscribersCache)
            {
                return remoteSubscribersCache.containsKey(composeSubscriberName(topic, subscriberName));
            }
        }

        /**
         * Retrieve all cached {@link com.max.messaging.subscribe.SubscriptionDetails}s
         *
         * @return {@code Collection}&lt;{@link com.max.messaging.subscribe.SubscriptionDetails}&gt;
         */
        public Collection<SubscriptionDetails> getCachedSubscribers()
        {
            synchronized (remoteSubscribersCache)
            {
                return remoteSubscribersCache.values();
            }
        }

        public SubscriptionDetails getCachedSubscriber(MaxTopic topic, String subscriberName)
        {
            synchronized (remoteSubscribersCache)
            {
                return remoteSubscribersCache.get(composeSubscriberName(topic, subscriberName));
            }
        }

        public void removeCachedSubscriber(MaxTopic topic, String subscriberName)
        {
            synchronized (remoteSubscribersCache)
            {
                remoteSubscribersCache.remove(composeSubscriberName(topic, subscriberName));
            }
        }

        private String composeSubscriberName(MaxTopic topic, String subscriberName)
        {
            return topic.name() + "_" + subscriberName;
        }
    }

    public Map<MaxTopic, TopicSettings> getTopicSettings()
    {
        return topicSettings;
    }

    public void setTopicSettings(Map<MaxTopic, TopicSettings> topicSettings)
    {
        this.topicSettings = topicSettings;
    }
}
