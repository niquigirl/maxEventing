package com.max.messaging.subscribe;

import com.max.messaging.TopicSettings;

/**
 * Interface for any Durable Topic Subscribers
 */
public interface DurableTopicSubscriber
{
    public void register(TopicSettings settings, SubscriptionDetails details) throws TopicManagementException;
    public void unregister(TopicSettings settings, SubscriptionDetails subscriber) throws TopicManagementException;
}
