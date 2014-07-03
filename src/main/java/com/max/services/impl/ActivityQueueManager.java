package com.max.services.impl;

import com.max.db.model.RemoteSubscriber;
import com.max.db.dao.RemoteSubscriberDao;
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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

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
public class ActivityQueueManager implements QueueManager, ApplicationContextAware
{
    public static final int DEFAULT_TIMEOUT = 30000;
    public static final String REMOTE_SUBSCRIBER_FACADE = "remoteSubscriberFacade";

    Logger log = Logger.getLogger(ActivityQueueManager.class);

    RemoteSubscriberDao remoteSubscriberDao;

    ApplicationContext applicationContext;

    @Autowired
    DurableTopicSubscriber topicSubscriber;

    Map<MaxTopic, TopicSettings> topicSettings;

    private boolean autoRegisterRemoteSubscribers;

    private static SubscriberCache cache = new SubscriberCache();

    public ActivityQueueManager(RemoteSubscriberDao remoteSubscriberDao, boolean autoRegisterRemoteSubscribers) throws InvalidSubscriberException
    {
        this.remoteSubscriberDao = remoteSubscriberDao;
        this.autoRegisterRemoteSubscribers = autoRegisterRemoteSubscribers;
    }


    /**
     * Publish a message to the Activity Topic
     *
     * @param msgObject {@link com.max.web.model.DefaultActivityMessage}
     * @throws javax.naming.NamingException
     * @throws javax.jms.JMSException
     */
    @Override
    public void sendMessage(MaxTopic maxTopic, String msgObject) throws NamingException, JMSException, InvalidMessageException, IOException
    {
        TopicSettings settings = getTopicSettings().get(maxTopic);
        log.debug("Request to send Message to : " + maxTopic + " : "  + msgObject);

        sendMessage(settings, msgObject);
    }

    /**
     * Public, but not exposed by the interface, this method should be used directly only for testing
     *
     * @param settings {@link com.max.messaging.TopicSettings}
     * @param msgObject {@code String} Should be JSON
     *
     * @throws NamingException
     * @throws JMSException
     * @throws InvalidMessageException
     * @throws IOException
     */
    public void sendMessage(TopicSettings settings, String msgObject)  throws NamingException, JMSException, InvalidMessageException, IOException
    {
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
     * Allows us to register a single remote subscriber. This method saves the subscriber to the DB and then goes through
     * and registers all subscribers
     *
     * @param subscription {@link com.max.web.model.RemoteSubscription}
     */
    @Override
    public void register(@NotNull RemoteSubscription subscription) throws TopicManagementException, InvalidSubscriberException
    {
        if (cache.isCached(subscription.getTopic(), subscription.getName()))
        {
            unregister(subscription.getTopic(), subscription.getName());
        }

        // TODO: THIS SHOULD BE PULLED OUT INTO A SERVICE THAT UNDERSTANDS THE REQUIREMENTS OF THE DB
        RemoteSubscriber curSubscriber = getRemoteSubscriberDao().findByTopicAndName(subscription.getTopic(), subscription.getName());

        if (curSubscriber != null)
        {
            validateSubscriber(curSubscriber);
            curSubscriber.setAutoRegister(true);
            curSubscriber.setFilterString(subscription.getFilterString());
        }
        else
            curSubscriber = subscription.toData();

        getRemoteSubscriberDao().save(curSubscriber);

        doRegister(curSubscriber);
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
            log.debug("Set message property " + curProperty.toString() + " to " + objectMessage.getStringProperty(curProperty.toString()));
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
     * Utility to handle "deactivating" the subscriber from the registry, meaning to set the autoRegister to false
     * TODO: could do this directly in SQL as an update statement
     *
     * @param subscriberName {@code String} Naming the remote subscriber to remove from the registry
     */
    private void deactivateSubscriberInRegistry(MaxTopic topic, @NotNull final String subscriberName)
    {
        final RemoteSubscriber remoteSubscriber = getRemoteSubscriberDao().findByTopicAndName(topic, subscriberName);
        if (remoteSubscriber != null)
        {
            remoteSubscriber.setAutoRegister(false);
            getRemoteSubscriberDao().save(remoteSubscriber);
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
        SubscriptionDetails details = buildSubscriptionDetails(curSubscriber);

        if (details != null)
        {
            topicSubscriber.register(getTopicSettings().get(curSubscriber.getTopic()), details);
            cache.cacheSubscriber(curSubscriber.getName(), details);
        }
        else
        {
            throw new InvalidSubscriberException("Couldn't build subscription details");
        }
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

        if (curFacade == null)
            return null;
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

    public RemoteSubscriberDao getRemoteSubscriberDao()
    {
        return remoteSubscriberDao;
    }

    public Logger getLog()
    {
        return log;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;

        if (autoRegisterRemoteSubscribers)
        {
            new Thread(new SubscriberAutoRegistrar()).start();
        }

        log.info("Successfully completed starting app");
    }

    class SubscriberAutoRegistrar implements Runnable
    {

        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                try
                {
                    Thread.sleep(60000);
                }
                catch (InterruptedException e)
                {
                    // Don't care
                }

                registerAllManagedListeners();
            }
        }

        /**
         * From all RemoteSubscribers found in the data store, register a new RemoteSubscriberFacade
         */
        public void registerAllManagedListeners()
        {
            getLog().info("Running AutoRegister for managed listeners: " + this);

            final Collection<RemoteSubscriber> remoteSubscribers = getRemoteSubscriberDao().findByAutoRegister(true);

            StringBuilder errorMessage = new StringBuilder("Failures when attempting to register all listeners from the registry:");
            boolean hasErrors = false;

            for (RemoteSubscriber curSubscriber : remoteSubscribers)
            {
                try
                {
                    if (!cache.isCached(curSubscriber.getTopic(), curSubscriber.getName()))
                        doRegister(curSubscriber);
                }
                catch (InvalidSubscriberException | TopicManagementException e)
                {
                    errorMessage.append(e.getMessage()).append(" | ");
                    hasErrors = true;
                }
            }

            if (hasErrors)
            {
                log.error("Failure to register subscribers on startup: " + errorMessage);
            }
        }


    }

    /**
     * Maintain a cache of all listeners
     */
    static class SubscriberCache
    {
        private final HashMap<String, SubscriptionDetails> remoteSubscribersCache = new HashMap<>();

        /**
         * Cache a {@link com.max.services.MaxMessageListener} under the subscriber name
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
