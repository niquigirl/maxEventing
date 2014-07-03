package com.max.services;

import com.max.messaging.MaxTopic;
import com.max.messaging.publish.InvalidMessageException;
import com.max.messaging.subscribe.TopicManagementException;
import com.max.web.model.RemoteSubscription;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Contract for all subscriber managers (of different queues/systems)
 */
public interface QueueManager
{
    @SuppressWarnings("unused")
//    public void registerAllManagedListeners() throws InvalidSubscriberException;
    public void register(@NotNull RemoteSubscription subscriber) throws InvalidSubscriberException, TopicManagementException;
    public void unregister(MaxTopic topic, @NotNull String subscriberName) throws TopicManagementException;
    public void sendMessage(MaxTopic maxTopic, String msgObject) throws NamingException, JMSException, InvalidMessageException, IOException;
}
