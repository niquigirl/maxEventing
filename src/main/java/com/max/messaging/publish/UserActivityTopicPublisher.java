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

package com.max.messaging.publish;

import com.max.messaging.TopicSettings;
import com.max.messaging.subscribe.UserActivityTopicFilterUtil;
import com.max.web.model.MaxMessage;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class UserActivityTopicPublisher
{
    private static final Logger log = Logger.getLogger(UserActivityTopicPublisher.class);

    private TopicSettings topicSettings;

    /**
     * Publish a message to the UserActivity Topic
     *
     * @param msgObject {@link com.max.web.model.MaxMessage}
     * @throws NamingException
     * @throws JMSException
     */
    // TODO: validate message and sender prior to sending
    public void sendMessage(MaxMessage msgObject) throws NamingException, JMSException, InvalidMessageException
    {
        log.debug("Request to sendMessage by MaxMessage: " + msgObject.toString());

        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, getTopicSettings().getQpidIcf());
        properties.put(getTopicSettings().getConnectionFactoryNamePrefix() + getTopicSettings().getConnectionFactoryName(), getTCPConnectionURL(getTopicSettings().getUserName(), getTopicSettings().getPassword()));
        properties.put(getTopicSettings().getTopicNamePrefix() + getTopicSettings().getTopicName(), getTopicSettings().getTopicName());

        InitialContext ctx = new InitialContext(properties);

        // Lookup connection factory
        TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(getTopicSettings().getConnectionFactoryName());
        TopicConnection topicConnection = connFactory.createTopicConnection();
        topicConnection.start();

        TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
        // Send message
        Topic topic = (Topic) ctx.lookup(getTopicSettings().getTopicName());
        // create the message to send
        TextMessage objectMessage = topicSession.createTextMessage(new JSONObject(msgObject).toString());

        setMessageProperties(objectMessage, msgObject);
        javax.jms.TopicPublisher topicPublisher = topicSession.createPublisher(topic);
        topicPublisher.send(objectMessage);
        System.out.println("Doing SEND");
        topicPublisher.close();
        topicSession.close();
        topicConnection.close();
    }

    /**
     * Take all the properties defined in the
     * @param objectMessage {@code javax.jms.TextMessage} High-level message on which to set metadata
     * @param msgObject {@link com.max.web.model.MaxMessage}
     * @throws JMSException
     */
    private void setMessageProperties(TextMessage objectMessage, MaxMessage msgObject) throws JMSException
    {
        Properties metaProperties = UserActivityTopicFilterUtil.getMetaPropertiesForPublish(msgObject);
        for (Object curProperty : metaProperties.keySet())
        {
            objectMessage.setStringProperty(curProperty.toString(), metaProperties.get(curProperty).toString());
        }
    }

    private String getTCPConnectionURL(String username, String password)
    {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        final String connectionUrl = "amqp://" + username + ":" + password + "@" +
                getTopicSettings().getCarbonClientId() + "/" + getTopicSettings().getCarbonVirtualHostName() + "?brokerlist='tcp://" + getTopicSettings().getCarbonDefaultHostname() + ":" + getTopicSettings().getCarbonDefaultPort() + "'";
        log.info("Connecting to " + connectionUrl);
        return connectionUrl;
    }


    public TopicSettings getTopicSettings()
    {
        return topicSettings;
    }

    public void setTopicSettings(TopicSettings topicSettings)
    {
        this.topicSettings = topicSettings;
    }
}
