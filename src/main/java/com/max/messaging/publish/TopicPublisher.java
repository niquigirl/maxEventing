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
import com.max.messaging.message.MaxMessage;
import com.max.messaging.message.MaxMessageUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;

public class TopicPublisher
{
    private static final Logger log = Logger.getLogger(TopicPublisher.class);

    private TopicSettings topicSettings;


    public void sendMessages(String topicName, String message) throws IOException, JSONException, JMSException, NamingException
    {
        log.debug("Request to sendMessage by String: " + message);

        MaxMessage msgObject = MaxMessage.getInstance(message);
        sendMessage(topicName, msgObject);
    }

    public void sendMessage(String topicName, MaxMessage msgObject) throws NamingException, JMSException
    {
        log.debug("Request to sendMessage by MaxMessage: " + msgObject.toString());

        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, topicSettings.getQpidIcf());
        properties.put(topicSettings.getConnectionFactoryNamePrefix() + topicSettings.getConnectionFactoryName(), getTCPConnectionURL(topicSettings.getUserName(), topicSettings.getPassword()));
        properties.put(topicSettings.getTopicNamePrefix() + topicName, topicName);

        InitialContext ctx = new InitialContext(properties);

        // Lookup connection factory
        TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(topicSettings.getConnectionFactoryName());
        TopicConnection topicConnection = connFactory.createTopicConnection();
        topicConnection.start();

        TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
        // Send message
        Topic topic = (Topic) ctx.lookup(topicName);
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

    private void setMessageProperties(TextMessage objectMessage, MaxMessage msgObject) throws JMSException
    {
        Properties metaProperties = MaxMessageUtils.getMetaProperties(msgObject);
        for (Object curProperty : metaProperties.keySet())
        {
            objectMessage.setStringProperty(curProperty.toString(), metaProperties.get(curProperty).toString());
        }
    }

    private String getTCPConnectionURL(String username, String password)
    {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return "amqp://" + username + ":" + password + "@" +
                topicSettings.getCarbonClientId() + "/" + topicSettings.getCarbonVirtualHostName() + "?brokerlist='tcp://" + topicSettings.getCarbonDefaultHostname() + ":" + topicSettings.getCarbonDefaultPort() + "'";
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
