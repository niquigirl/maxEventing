package com.max.services;

import com.max.coaching.db.model.RemoteSubscriber;
import com.max.messaging.subscribe.TopicManagementException;

import javax.validation.constraints.NotNull;

/**
 * Contract foro all subscriber managers (of different queues/systems)
 */
public interface SubscriberManager
{
    @SuppressWarnings("unused")
    public void registerAllManagedListeners() throws InvalidSubscriberException;
    public void register(@NotNull RemoteSubscriber subscriber) throws InvalidSubscriberException, TopicManagementException;
    public void unregister(@NotNull String subscriberName) throws TopicManagementException;
}
