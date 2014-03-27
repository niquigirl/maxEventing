package com.max.messaging.listeners.impl;

import com.max.messaging.listeners.DurableTopicSubscriber;
import com.max.messaging.message.MaxMessage;
import com.max.services.MailService;
import com.max.services.impl.MailServiceImpl;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.wso2.andes.client.message.JMSTextMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TopicSubscriber;
import java.io.IOException;
import java.util.Properties;

/**
 * Topic subscriber which handles tasks related to notifications
 */
public class NotificationSubscriber extends DurableTopicSubscriber implements MessageListener
{
    private TopicSubscriber topicSubscriber;

    private MailService mailService = new MailServiceImpl();

    Logger log = Logger.getLogger(NotificationSubscriber.class);

    @Override
    public void onMessage(Message message)
    {
        System.out.println("Starting onMessage " + Thread.currentThread().getId());
        try
        {
            String messageText = ((JMSTextMessage) message).getText();

            try
            {
                MaxMessage maxMessage = MaxMessage.getInstance(messageText);
                log.info("NotificationSubscriber subscribes to this message: " + messageText);
                mailService.sendMail("niqui@max.com", maxMessage.getVerb(),
                        maxMessage.getDescription().get("us") + " happened for " + maxMessage.getActor().getObjectType() +
                            " with ID " + maxMessage.getActor().getId());
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

    public NotificationSubscriber(String topicName, Properties filters)
    {
        super();
        super.registerSubscriber(topicName, filters);
    }


    @Override
    public String getSubscriptionId()
    {
        return "NotificationEngine";
    }

}
