package com.max.messaging;

import com.max.BaseSpringInjectionUnitTest;
import com.max.messaging.message.MaxMessage;
import com.max.web.services.TopicPublisherService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Validate subscribing to the mail Topic
 */
public class DurableTopicSubscriberTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    TopicPublisherService topicPublisherService;

    @Test
    public void testReceiveMessages() throws Exception
    {
        MaxMessage message = new MaxMessage();
        MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setDisplayName("Niqui Van Eastman");
        actor.setId(55);
        actor.setObjectType("PreferredCustomer");
        message.setActor(actor);
        message.setVerb("ProspectContacted");
        message.setLanguage("en");

        MaxMessage message2 = new MaxMessage();
        MaxMessage.Actor actor2 = new MaxMessage.Actor();
        actor2.setDisplayName("NewAssociate Sam");
        actor2.setId(513674);
        actor2.setObjectType("Associate");
        message2.setActor(actor2);
        message2.setVerb("AssociateSignedUp");
        message2.setLanguage("en");

        topicPublisherService.publish(message);
        topicPublisherService.publish(message2);

        Thread.sleep(3000);
    }

}
