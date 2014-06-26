/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package com.max.messaging.wso2;

import com.max.messaging.TopicSettings;
import com.max.messaging.subscribe.DurableTopicSubscriber;
import com.max.messaging.subscribe.SubscriptionDetails;
import com.max.messaging.subscribe.TopicManagementException;
import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class WSO2DurableTopicSubscriber implements DurableTopicSubscriber
{
    Logger log = Logger.getLogger(WSO2DurableTopicSubscriber.class);


    @Override
    public void register(TopicSettings settings, SubscriptionDetails details) throws TopicManagementException
    {
        TopicConnection topicConnection;
        TopicSession topicSession;

        TopicSubscriber topicSubscriber;

        try
        {
            log.info("-- Registering listener " + details.getSubscriberName() + " to topic " + settings.getTopicName() + " with filter string " + details.getFilterString());
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, settings.getQpidIcf());
            properties.put(settings.getConnectionFactoryNamePrefix() + settings.getConnectionFactoryName(), getTCPConnectionURL(settings));
            properties.put(settings.getTopicNamePrefix() + settings.getTopicName(), settings.getTopicName());

            InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(settings.getConnectionFactoryName());
            topicConnection = connFactory.createTopicConnection();
            topicConnection.start();
            topicSession = topicConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);

            // create durable subscriber with subscription ID
            Topic topic = (Topic) ctx.lookup(settings.getTopicName());
            topicSubscriber = topicSession.createDurableSubscriber(topic, details.getSubscriberName(), details.getFilterString(), false);

            topicSubscriber.setMessageListener(details.getListener());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new TopicManagementException("Failed to register listener", e);
        }
    }

    @Override
    public void unregister(TopicSettings settings, String subscriberName) throws TopicManagementException
    {
        TopicConnection topicConnection;
        TopicSession topicSession;

        log.info("Unregistering " + subscriberName + " from Topic " + settings.getTopicName());

        try
        {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, settings.getQpidIcf());
            properties.put(settings.getConnectionFactoryNamePrefix() + settings.getConnectionFactoryName(), getTCPConnectionURL(settings));
            properties.put(settings.getTopicNamePrefix() + settings.getTopicName(), settings.getTopicName());

            InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(settings.getConnectionFactoryName());
            topicConnection = connFactory.createTopicConnection();
//            setTopicConnection(topicConnection);
            topicConnection.start();
//            setTopicSession(topicConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE));
            topicSession = topicConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);

//            setTopicSubscriber(getTopicSession().createDurableSubscriber(topic, details.getSubscriberName(), details.getFilterString(), false));
            topicSession.unsubscribe(subscriberName);
        }
        catch (NamingException | JMSException e)
        {
            e.printStackTrace();
            throw new TopicManagementException("Could not unregister " + subscriberName + " " + e.getMessage());
        }
    }

    public String getTCPConnectionURL(TopicSettings settings)
    {
        String username = settings.getUserName();
        String password = settings.getPassword();
        String carbonClientId = settings.getCarbonClientId();
        String carbonVirtualHostName = settings.getCarbonVirtualHostName();
        String carbonDefaultHostname = settings.getCarbonDefaultHostname();
        String carbonDefaultPort = settings.getCarbonDefaultPort();

        return "amqp://" + username + ":" + password + "@" + carbonClientId + "/" + carbonVirtualHostName + "?brokerlist='tcp://" + carbonDefaultHostname + ":" + carbonDefaultPort + "'";
    }

}


/*
    public void register()
    {
        try
        {
            TopicSettings settings = getTopicSettings();

            System.out.println("Starting the subscriber " + this);
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, settings.getQpidIcf());
            properties.put(settings.getConnectionFactoryNamePrefix() + settings.getConnectionFactoryName(), getTCPConnectionURL());
            properties.put(settings.getTopicNamePrefix() + settings.getTopicName(), settings.getTopicName());

            InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(getTopicSettings().getConnectionFactoryName());
            TopicConnection topicConnection = connFactory.createTopicConnection();
            setTopicConnection(topicConnection);
            topicConnection.start();
            setTopicSession(topicConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE));

            // create durable subscriber with subscription ID
            Topic topic = (Topic) ctx.lookup(settings.getTopicName());
            System.out.println("-- Registering " + getSubscriberName() + " with filter " + getFilterString());
            setTopicSubscriber(getTopicSession().createDurableSubscriber(topic, getSubscriberName(), getFilterString(), false));

            getTopicSubscriber().setMessageListener(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
*/
