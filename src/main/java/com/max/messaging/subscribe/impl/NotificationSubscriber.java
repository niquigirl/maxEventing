package com.max.messaging.subscribe.impl;

import com.max.messaging.subscribe.DurableTopicSubscriber;
import com.max.messaging.message.MaxMessage;
import com.max.services.MailService;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.andes.client.message.JMSTextMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;

/**
 * Topic subscriber which handles tasks related to notifications
 */
public class NotificationSubscriber extends DurableTopicSubscriber
{
    private MailService mailService;

    Logger log = Logger.getLogger(NotificationSubscriber.class);

    @Override
    public void onMessage(MaxMessage message)
    {
        System.out.println("Starting onMessage " + Thread.currentThread().getId());
        try
        {
            log.info("NotificationSubscriber subscribes to this message: " + message);
            mailService.sendMail("niqui@max.com", message.getVerb(),
                    (message.getDescription() != null ? message.getDescription().getEn() : "No Description") +
                            " happened for " +                                (message.getActor() != null ? message.getActor().getObjectType() : "No Actor") +
                            " with ID " +
                            (message.getActor() != null ? message.getActor().getId() : "No Actor"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public MailService getMailService()
    {
        return mailService;
    }

    public void setMailService(MailService mailService)
    {
        this.mailService = mailService;
    }
}
