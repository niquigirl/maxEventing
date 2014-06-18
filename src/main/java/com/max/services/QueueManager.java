package com.max.services;

import com.max.db.model.RemoteSubscriber;
import com.max.messaging.MaxTopic;
import com.max.messaging.publish.InvalidMessageException;
import com.max.messaging.subscribe.TopicManagementException;
import com.max.web.model.DefaultActivityMessage;
import org.json.JSONException;

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
    public void registerAllManagedListeners() throws InvalidSubscriberException;
    public void register(@NotNull RemoteSubscriber subscriber) throws InvalidSubscriberException, TopicManagementException;
    public void unregister(@NotNull String subscriberName) throws TopicManagementException;
    public void sendMessage(MaxTopic maxTopic, String msgObject) throws NamingException, JMSException, InvalidMessageException, IOException, JSONException;
}
