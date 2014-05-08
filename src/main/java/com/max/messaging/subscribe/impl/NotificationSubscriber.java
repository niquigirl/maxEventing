package com.max.messaging.subscribe.impl;

import com.max.coaching.db.model.NotificationTemplate;
import com.max.coaching.db.repositories.NotificationTemplateRepository;
import com.max.messaging.MessageUtils;
import com.max.messaging.publish.TopicPublisher;
import com.max.messaging.subscribe.DurableTopicSubscriber;
import com.max.messaging.message.MaxMessage;
import com.max.services.MailService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;

/**
 * Topic subscriber which handles notifications
 */
public class NotificationSubscriber extends DurableTopicSubscriber
{
    private MailService mailService;

    @Autowired
    TopicPublisher topicPublisher;

    @Autowired
    NotificationTemplateRepository notificationTemplateRepository;

    Logger log = Logger.getLogger(NotificationSubscriber.class);

    @Override
    public void onMessage(MaxMessage message)
    {
        System.out.println("Starting NotificationSubscriber onMessage");
        try
        {
            log.info("NotificationSubscriber subscribes to this message: " + message);

            List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findByEventName(message.getVerb());

            for (NotificationTemplate curTemplate : notificationTemplates)
            {
                log.info("Processing NotificationTemplate " + curTemplate.toString());

                getMailService().sendMail("niqui@max.com", message.getVerb(),
                        (MessageUtils.getNotificationMessage(message.getVerb() + "_" + curTemplate.getRecipientType(), message.getLanguage())));

                if (!message.getVerb().equalsIgnoreCase("SendingEmail"))
                {
                    MaxMessage sendingEmailMessage = new MaxMessage();
                    sendingEmailMessage.setVerb("SendingEmail");
                    topicPublisher.sendMessage(topicPublisher.getTopicSettings().getTopicName(), sendingEmailMessage); // TODO
                }
            }
        }
        catch (IOException | JMSException | NamingException e)
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
