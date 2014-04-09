package com.max.messaging.subscribe.impl;

import com.max.BaseSpringInjectionUnitTest;
import com.max.messaging.MessageUtils;
import com.max.messaging.message.MaxMessage;
import com.max.services.MailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Verify Notification Subscriber functionality
 */
public class NotificationSubscriberTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    MailService mailService;
    @Test
    public void testOnMessage() throws Exception
    {
        MaxMessage message = new MaxMessage();
        message.setVerb("LoggedIn");
        String notificationMessage = MessageUtils.getWebFeedMessage(message.getVerb(), message.getLanguage());
        mailService.sendMail("niqui@max.com", message.getVerb(),
                notificationMessage);

    }
}
