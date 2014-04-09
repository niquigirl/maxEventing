package com.max.messaging.subscribe.impl;

import com.max.messaging.MessageUtils;
import com.max.messaging.subscribe.DurableTopicSubscriber;
import com.max.messaging.message.MaxMessage;
import com.max.services.MailService;

import org.apache.log4j.Logger;

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
        System.out.println("Starting NotificationSubscriber onMessage");
        try
        {
            log.info("NotificationSubscriber subscribes to this message: " + message);
            getMailService().sendMail("niqui@max.com", message.getVerb(),
                    (MessageUtils.getNotificationMessage(message.getVerb(), message.getLanguage())));
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
