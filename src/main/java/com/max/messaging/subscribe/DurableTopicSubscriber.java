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

package com.max.messaging.subscribe;

import com.max.messaging.TopicSettings;
import com.max.messaging.message.MaxMessage;
import com.max.services.MailService;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.andes.client.message.JMSTextMessage;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.IOException;
import java.util.Properties;

public abstract class DurableTopicSubscriber implements MessageListener
{
    Logger log = Logger.getLogger(DurableTopicSubscriber.class);

    @Autowired
    MailService mailService;

    private TopicSettings topicSettings;
    private TopicConnection topicConnection;
    private TopicSession topicSession;

    private TopicSubscriber topicSubscriber;

    private String filterString;
    private String subscriberName;

    public abstract void onMessage(MaxMessage maxMessage);

    @Override
    public void onMessage(Message message)
    {
        System.out.println("Starting onMessage " + this);
        try
        {
            String messageText = ((JMSTextMessage) message).getText();

            try
            {
                MaxMessage maxMessage = MaxMessage.getInstance(messageText);
                onMessage(maxMessage);
            }
            catch (JSONException e)
            {
                mailService.sendErrorEmail("MaxMessaging - Error", "Could not generate a MaxMessage from the following<br/>" + messageText);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (JMSException e)
        {
            e.printStackTrace();
        }
    }





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

    public void doFinalize()
    {
        try
        {
            log.debug("*****Executing finalize . . .");

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
        System.out.println("****************Closing Subscriber " + getSubscriberName());
        log.warn("****************Closing Subscriber " + getSubscriberName());
    }

    public String getTCPConnectionURL()
    {
        TopicSettings settings = getTopicSettings();
        String username = settings.getUserName();
        String password = settings.getPassword();
        String carbonClientId = settings.getCarbonClientId();
        String carbonVirtualHostName = settings.getCarbonVirtualHostName();
        String carbonDefaultHostname = settings.getCarbonDefaultHostname();
        String carbonDefaultPort = settings.getCarbonDefaultPort();

        return "amqp://" + username + ":" + password + "@" + carbonClientId + "/" + carbonVirtualHostName + "?brokerlist='tcp://" + carbonDefaultHostname + ":" + carbonDefaultPort + "'";
    }

    public TopicSession getTopicSession()
    {
        return topicSession;
    }

    public String getFilterString()
    {
        return filterString;
    }

    public void setFilterString(String filterString)
    {
        this.filterString = filterString;
    }

    public String getSubscriberName()
    {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName)
    {
        this.subscriberName = subscriberName;
    }


    public TopicConnection getTopicConnection()
    {
        return topicConnection;
    }

    public TopicSubscriber getTopicSubscriber()
    {
        return topicSubscriber;
    }

    protected void setTopicConnection(TopicConnection topicConnection)
    {
        this.topicConnection = topicConnection;
    }

    protected void setTopicSession(TopicSession topicSession)
    {
        this.topicSession = topicSession;
    }

    protected void setTopicSubscriber(TopicSubscriber topicSubscriber)
    {
        this.topicSubscriber = topicSubscriber;
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
