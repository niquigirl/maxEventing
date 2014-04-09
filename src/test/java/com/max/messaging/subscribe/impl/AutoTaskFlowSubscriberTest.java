package com.max.messaging.subscribe.impl;

import com.max.BaseSpringInjectionUnitTest;
import com.max.messaging.message.MaxMessage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by neastman on 4/8/14.
 */
public class AutoTaskFlowSubscriberTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    AutoTaskFlowSubscriber autoTaskFlowSubscriber;

    @Test
    public void testOnMessage() throws Exception
    {
        MaxMessage message = new MaxMessage();
        message.setVerb("LoggedIn");
        MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setId(123);
        actor.setObjectType("Associate");
        message.setActor(actor);

        autoTaskFlowSubscriber.onMessage(message);

        Thread.sleep(3000);
    }
}
