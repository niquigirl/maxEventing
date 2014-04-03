package com.max.messaging.subscribe;

import com.max.messaging.TopicSettings;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.List;
import java.util.Properties;

/**
 * This guy manages the starting/stopping and stuff for the subscribers to messages that live natively in this system.
 * TODO: In the future, will manage remote subscribers
 */
public class SubscriberRegistry
{
    List<DurableTopicSubscriber> durableTopicSubscribers;

    public void init()
    {
        if (durableTopicSubscribers == null)
            return;

        for (DurableTopicSubscriber curSub : getDurableTopicSubscribers())
        {
            registerSubscriber(curSub);
        }
    }

    public void destroy()
    {
        if (durableTopicSubscribers == null)
            return;

        for (DurableTopicSubscriber curSub : getDurableTopicSubscribers())
        {
            curSub.doFinalize();
        }

    }
    public void registerSubscriber(DurableTopicSubscriber subscriber)
    {
        try
        {
            TopicSettings settings = subscriber.getTopicSettings();
            
            System.out.println("Starting the subscriber " + this);
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, settings.getQpidIcf());
            properties.put(settings.getConnectionFactoryNamePrefix() + settings.getConnectionFactoryName(), getTCPConnectionURL(subscriber));
            properties.put(settings.getTopicNamePrefix() + settings.getTopicName(), settings.getTopicName());

            InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(subscriber.getTopicSettings().getConnectionFactoryName());
            TopicConnection topicConnection = connFactory.createTopicConnection();
            subscriber.setTopicConnection(topicConnection);
            topicConnection.start();
            subscriber.setTopicSession(topicConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE));

            // create durable subscriber with subscription ID
            Topic topic = (Topic) ctx.lookup(settings.getTopicName());
            System.out.println("-- Registering " + subscriber.getSubscriberName() + " with filter " + subscriber.getFilterString());
            subscriber.setTopicSubscriber(subscriber.getTopicSession().createDurableSubscriber(topic, subscriber.getSubscriberName(), subscriber.getFilterString(), false));

            subscriber.getTopicSubscriber().setMessageListener(subscriber);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getTCPConnectionURL(DurableTopicSubscriber subscriber)
    {
        TopicSettings settings = subscriber.getTopicSettings();
        String username = settings.getUserName();
        String password = settings.getPassword();
        String carbonClientId = settings.getCarbonClientId();
        String carbonVirtualHostName = settings.getCarbonVirtualHostName();
        String carbonDefaultHostname = settings.getCarbonDefaultHostname();
        String carbonDefaultPort = settings.getCarbonDefaultPort();

        return "amqp://" + username + ":" + password + "@" + carbonClientId + "/" + carbonVirtualHostName + "?brokerlist='tcp://" + carbonDefaultHostname + ":" + carbonDefaultPort + "'";
    }

    public List<DurableTopicSubscriber> getDurableTopicSubscribers()
    {
        return durableTopicSubscribers;
    }

    public void setDurableTopicSubscribers(List<DurableTopicSubscriber> durableTopicSubscribers)
    {
        this.durableTopicSubscribers = durableTopicSubscribers;
    }
}
