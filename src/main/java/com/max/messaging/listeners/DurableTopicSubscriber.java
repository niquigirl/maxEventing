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

package com.max.messaging.listeners;

import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public abstract class DurableTopicSubscriber implements MessageListener
{
    Logger log = Logger.getLogger(DurableTopicSubscriber.class);


    public static final String ANDES_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String CF_NAME = "andesConnectionfactory";
    private static final String userName = "admin";
    private static final String password = "admin";
    private static String CARBON_CLIENT_ID = "carbon";
    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private static String CARBON_DEFAULT_HOSTNAME = "localhost";
    private static String CARBON_DEFAULT_PORT = "5672";
    private TopicConnection topicConnection;
    private TopicSession topicSession;

    private TopicSubscriber topicSubscriber;

    public TopicSession getTopicSession()
    {
        return topicSession;
    }
    /**
     * Required; subscribers to identify themselves
     *
     * @return {@code String}
     */
    public abstract String getSubscriptionId();

    public DurableTopicSubscriber()
    {
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    public TopicSession registerSubscriber(String topicName, Properties filters)
    {
        try
        {
            System.out.println("Starting the subscriber " + this);
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, ANDES_ICF);
            properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
            properties.put("topic." + topicName, topicName);

            InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(CF_NAME);
            topicConnection = connFactory.createTopicConnection();
            topicConnection.start();
            topicSession = topicConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);

            // create durable subscriber with subscription ID
            Topic topic = (Topic) ctx.lookup(topicName);
            setTopicSubscriber(topicSession.createDurableSubscriber(topic, getSubscriptionId(), "verb = 'NewAssociate'", false));

            topicSubscriber.setMessageListener(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return topicSession;
    }

    public String getTCPConnectionURL(String username, String password)
    {
        return new StringBuffer()
                .append("amqp://").append(username).append(":").append(password)
                .append("@").append(CARBON_CLIENT_ID)
                .append("/").append(CARBON_VIRTUAL_HOST_NAME)
                .append("?brokerlist='tcp://").append(CARBON_DEFAULT_HOSTNAME).append(":").append(CARBON_DEFAULT_PORT).append("'")
                .toString();
    }

    public TopicSubscriber getTopicSubscriber()
    {
        return topicSubscriber;
    }

    public void setTopicSubscriber(TopicSubscriber topicSubscriber)
    {
        this.topicSubscriber = topicSubscriber;
    }

    public void doFinalize()
    {
        try
        {
            log.debug("*****Executing shutdown hook . . .");

            if (topicSubscriber != null)
                topicSubscriber.close();
            if (topicSession != null)
                topicSession.close();
            if (topicConnection != null)
                topicConnection.close();
        }
        catch (final JMSException e)
        {
            // Exit
        }
        System.out.println("****************Closing Subscriber " + getSubscriptionId());
        log.warn("****************Closing Subscriber " + getSubscriptionId());
    }

    /**
     * A shutdown hook that stops the "other thread" upon JVM shutdown. If the JVM exits abruptly, without
     * an orderly Spring lifecycle shutdown, this makes sure the "other thread" is stopped cleanly.
     */
    private final class ShutdownHook extends Thread
    {
        @Override
        public void run()
        {

            try
            {
                log.debug("*****Executing shutdown hook . . .");

                if (topicSubscriber != null)
                    topicSubscriber.close();
                if (topicSession != null)
                    topicSession.close();
                if (topicConnection != null)
                    topicConnection.close();
            }
            catch (final JMSException e)
            {
                // Exit
            }
        }
    }

}
