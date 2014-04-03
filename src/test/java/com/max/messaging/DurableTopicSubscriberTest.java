package com.max.messaging;

import com.max.BaseSpringInjectionUnitTest;
import com.max.messaging.message.MaxMessage;
import com.max.messaging.subscribe.impl.NotificationSubscriber;
import com.max.web.services.TopicPublisherService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Validate subscribing to the mail Topic
 */
public class DurableTopicSubscriberTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    NotificationSubscriber notificationSubscriber;

    @Autowired
    TopicPublisherService topicPublisherService;

    @Test
    public void testReceiveMessages() throws Exception
    {
        MaxMessage message = new MaxMessage();
        MaxMessage.LanguageText description = new MaxMessage.LanguageText();
        description.setEn("One thing - not important to the Notifier");
        MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setDisplayName("Niqui Van Eastman");
        actor.setId(55);
        actor.setObjectType("PreferredCustomer");
        message.setDescription(description);
        message.setActor(actor);
        message.setVerb("OneThing");
        message.setLanguage("en");

        MaxMessage message2 = new MaxMessage();
        MaxMessage.LanguageText description2 = new MaxMessage.LanguageText();
        message2.setDescription(description2);
        description2.setEn("New Associate - should trigger the Notifier");
        MaxMessage.Actor actor2 = new MaxMessage.Actor();
        actor2.setDisplayName("NewAssociate Sam");
        actor2.setId(5454);
        actor2.setObjectType("Associate");
        message2.setActor(actor2);
        message2.setVerb("NewAssociate");
        message2.setLanguage("en");

        topicPublisherService.publish(message);
        topicPublisherService.publish(message2);

        Thread.sleep(20000);
    }

}
