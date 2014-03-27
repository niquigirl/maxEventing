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

import com.max.messaging.message.MaxMessage;
import org.apache.log4j.Logger;
import org.json.JSONException;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;

public class TopicPublisher
{
    private static final Logger log = Logger.getLogger(TopicPublisher.class);

    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String TOPIC_NAME_PREFIX = "topic.";
    private static final String CF_NAME = "tpidConnectionfactory";
    private static final String userName = "admin";
    private static final String password = "admin";
    private final static String CARBON_CLIENT_ID = "carbon";
    private final static String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private final static String CARBON_DEFAULT_HOSTNAME = "localhost";
    private final static String CARBON_DEFAULT_PORT = "5672";

    private static TopicPublisher topicPublisher;

    private TopicPublisher() {}

    public static TopicPublisher getInstance()
    {
        if (topicPublisher == null)
            topicPublisher = new TopicPublisher();

        return topicPublisher;
    }

    public void sendMessages(String topicName, String message) throws NamingException, JMSException
    {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
        properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
        properties.put(TOPIC_NAME_PREFIX + topicName, topicName);

        try
        {
            InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(CF_NAME);
            TopicConnection topicConnection = connFactory.createTopicConnection();
            topicConnection.start();

            TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            // Send message
            Topic topic = (Topic) ctx.lookup(topicName);
            // create the message to send
            TextMessage objectMessage = topicSession.createTextMessage(message);

            MaxMessage msgObject = MaxMessage.getInstance(message);
            setMessageProperties(objectMessage, msgObject);
            javax.jms.TopicPublisher topicPublisher = topicSession.createPublisher(topic);
            topicPublisher.send(objectMessage);
            System.out.println("Doing SEND");
            topicPublisher.close();
            topicSession.close();
            topicConnection.close();
        }
        catch (JSONException | IOException e)
        {
            log.error("Problem publishing message.  Body: " + message);
        }
    }

    private void setMessageProperties(TextMessage objectMessage, MaxMessage msgObject) throws JMSException
    {
        Properties metaProperties = msgObject.getMetaProperties();
        for (Object curProperty : metaProperties.keySet())
        {
            objectMessage.setStringProperty(curProperty.toString(), metaProperties.get(curProperty).toString());
        }
    }

    private String getTCPConnectionURL(String username, String password)
    {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return new StringBuilder()
                .append("amqp://").append(username).append(":").append(password)
                .append("@").append(CARBON_CLIENT_ID)
                .append("/").append(CARBON_VIRTUAL_HOST_NAME)
                .append("?brokerlist='tcp://").append(CARBON_DEFAULT_HOSTNAME).append(":").append(CARBON_DEFAULT_PORT).append("'")
                .toString();
    }

}
