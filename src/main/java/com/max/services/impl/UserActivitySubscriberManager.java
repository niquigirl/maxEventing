package com.max.services.impl;

import com.max.coaching.db.model.RemoteSubscriber;
import com.max.coaching.db.model.RemoteSubscriptionCriteria;
import com.max.coaching.db.repositories.RemoteSubscriberRepository;
import com.max.messaging.TopicSettings;
import com.max.messaging.subscribe.DurableTopicSubscriber;
import com.max.messaging.subscribe.MaxMessageListener;
import com.max.messaging.subscribe.SubscriptionDetails;
import com.max.messaging.subscribe.TopicManagementException;
import com.max.services.InvalidSubscriberException;
import com.max.services.SubscriberManager;
import com.max.web.model.HandlerResults;
import com.max.web.model.MaxMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
@Service
public class UserActivitySubscriberManager implements SubscriberManager
{
    public static final int DEFAULT_TIMEOUT = 30000;
    public static final String REMOTE_SUBSCRIBER_FACADE = "remoteSubscriberFacade";

    Logger log = Logger.getLogger(UserActivitySubscriberManager.class);

    @Autowired
    RemoteSubscriberRepository remoteSubscriberRepository;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DurableTopicSubscriber userActivityDurableTopicSubscriber;

    @Autowired
    TopicSettings userActivityTopicSettings;

    private static SubscriberCache cache = new SubscriberCache();

    /**
     * From all RemoteSubscribers found in the data store, register a new RemoteSubscriberFacade
     */
    @PostConstruct
    public void registerAllManagedListeners() throws InvalidSubscriberException
    {
        getLog().info("Running post construct on RemoteSubscriberFacadeFactory: " + this);
        final List<RemoteSubscriber> remoteSubscribers = getRemoteSubscriberRepository().findAll();

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
     * @param subscriber {@link com.max.coaching.db.model.RemoteSubscriber}
     */
    public void register(@NotNull RemoteSubscriber subscriber) throws TopicManagementException, InvalidSubscriberException
    {
        doRegister(subscriber);
        getRemoteSubscriberRepository().save(subscriber);
    }

    @Override
    public void unregister(@NotNull String subscriberName) throws TopicManagementException
    {
        userActivityDurableTopicSubscriber.unregister(userActivityTopicSettings, subscriberName);
    }

    /**
     * This method makes sure all validation passes and, if so, registers and caches the Subscription
     *
     * @param curSubscriber {@link com.max.coaching.db.model.RemoteSubscriber}
     * @throws InvalidSubscriberException In the event of any validation failures
     * @throws com.max.messaging.subscribe.TopicManagementException in the event of some failure registering
     */
    private void doRegister(RemoteSubscriber curSubscriber) throws InvalidSubscriberException, TopicManagementException
    {
        validateSubscriber(curSubscriber);
        if (curSubscriber.isAutoRegister() && !cache.isCached(curSubscriber.getName()))
        {
            SubscriptionDetails details = buildSubscriptionDetails(curSubscriber);

            final HandlerResults handlerResults = details.getListener().onTest(MaxMessage.generateTestInstance());
            if (handlerResults == null || !handlerResults.isSuccess())
            {
                throw new InvalidSubscriberException("Subscriber " + curSubscriber.getName() + " Failed test");
            }

            cache.cacheListener(curSubscriber.getName(), details.getListener());
            userActivityDurableTopicSubscriber.register(userActivityTopicSettings, details);
        }
    }

    /**
     * Runs through basic validation of the subscriber
     *
     * @param subscriber {@link com.max.coaching.db.model.RemoteSubscriber}
     * @throws InvalidSubscriberException
     */
    private void validateSubscriber(RemoteSubscriber subscriber) throws InvalidSubscriberException
    {
        StringBuilder errors = new StringBuilder();
        if (StringUtils.isEmpty(subscriber.getTestUrl()))
            errors.append("Test URL is required ");

        if (StringUtils.isEmpty(subscriber.getRestUrl()))
            errors.append(errors.length() > 0 ? ", " : "").append("REST URL is required ");

        if (StringUtils.isEmpty(subscriber.getName()))
            errors.append(errors.length() > 0 ? ", " : "").append("Name is required ");

        if (errors.length() > 0)
            throw new InvalidSubscriberException(errors.toString());
    }

    /**
     * This helper method composes a message selector string based on the criteria attached to the {@link com.max.coaching.db.model.RemoteSubscriber}
     *
     * @param curSubscriber {@link com.max.coaching.db.model.RemoteSubscriber}
     * @return {@code String}
     */
    protected String generateFilterString(RemoteSubscriber curSubscriber)
    {
        StringBuilder allFilters = new StringBuilder();
        if (curSubscriber.getRemoteSubscriptionCriteria() != null)
        {

            for (RemoteSubscriptionCriteria curCriteria : curSubscriber.getRemoteSubscriptionCriteria())
            {
                StringBuilder filterString = new StringBuilder();
                if (StringUtils.hasText(curCriteria.getVerb()))
                    filterString.append(MaxMessage.VERB).append(" = '").append(curCriteria.getVerb()).append("'");
                else if (StringUtils.hasText(curCriteria.getSubjectType()))
                    filterString.append(MaxMessage.SUBJECT_OBJECT_TYPE).append(" = '").append(curCriteria.getSubjectType()).append("'");
                else if (StringUtils.hasText(curCriteria.getActorType()))
                    filterString.append(MaxMessage.ACTOR_OBJECT_TYPE).append(" = '").append(curCriteria.getActorType()).append("'");

                if (filterString.length() > 0)
                    allFilters.append(allFilters.length() == 0 ? "" : " OR ").append(filterString);
            }
        }

        return allFilters.toString();
    }

    /**
     * Go through all cached listeners and run their test method
     */
    public void printStats()
    {
        for (MaxMessageListener curListener : cache.getCachedListeners())
        {
            getLog().info("***** Listener " + curListener);
            final HandlerResults handlerResults = curListener.onTest(MaxMessage.generateTestInstance());
            getLog().debug(handlerResults);

            if (handlerResults == null || !handlerResults.isSuccess())
            {
                getLog().warn("Listener " + curListener + " returned a bad HandlerResults");
            }
        }
    }

    /**
     * Build Subscription details from the RemoteSubscriber data
     *
     * @param curSubscriber {@link com.max.coaching.db.model.RemoteSubscriber}
     * @return {@link com.max.messaging.subscribe.SubscriptionDetails}
     */
    private SubscriptionDetails buildSubscriptionDetails(RemoteSubscriber curSubscriber)
    {
        RemoteSubscriberFacade curFacade = generateRemoteSubscriber(curSubscriber);

        SubscriptionDetails details = new SubscriptionDetails();
        details.setListener(curFacade);
        details.setFilterString(generateFilterString(curSubscriber));

        details.setSubscriberName(curSubscriber.getName());
        return details;
    }

    /**
     * Create a new {@link com.max.services.impl.RemoteSubscriberFacade} from the {@link com.max.coaching.db.model.RemoteSubscriber}
     *
     * @param curSubscriber {@link com.max.coaching.db.model.RemoteSubscriber}
     * @return {@link com.max.services.impl.RemoteSubscriberFacade}
     */
    private RemoteSubscriberFacade generateRemoteSubscriber(RemoteSubscriber curSubscriber)
    {
        RemoteSubscriberFacade curFacade = (RemoteSubscriberFacade) applicationContext.getBean(REMOTE_SUBSCRIBER_FACADE);
        curFacade.setServiceUrl(curSubscriber.getRestUrl());
        curFacade.setTestUrl(curSubscriber.getTestUrl());
        curFacade.setTimeoutMS(curSubscriber.getTimeout() == null ? DEFAULT_TIMEOUT : curSubscriber.getTimeout());

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
        private HashMap<String, MaxMessageListener> remoteSubscribersCache = new HashMap<>();

        /**
         * Cache a {@link com.max.messaging.subscribe.MaxMessageListener} under the subscriber name
         *
         * @param subscriberName {@code String}
         * @param listener       {@link com.max.messaging.subscribe.MaxMessageListener}
         */
        public void cacheListener(String subscriberName, MaxMessageListener listener)
        {
            remoteSubscribersCache.put(subscriberName, listener);
        }

        /**
         * Determine if there is a subscriber cached by the name provided
         *
         * @param subscriberName {@code String}
         * @return {@code boolean} where true indicates the subscriber name was found in the cache
         */
        public boolean isCached(String subscriberName)
        {
            return remoteSubscribersCache.containsKey(subscriberName);
        }

        /**
         * Retrieve all cached {@link com.max.messaging.subscribe.MaxMessageListener}s
         *
         * @return {@code Collection}&lt;{@link com.max.messaging.subscribe.MaxMessageListener}&gt;
         */
        public Collection<MaxMessageListener> getCachedListeners()
        {
            return remoteSubscribersCache.values();
        }
    }
}
