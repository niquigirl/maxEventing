package com.max.web.controller;

import com.max.BaseSpringInjectionUnitTest;
import com.max.db.model.RemoteSubscriber;
import com.max.messaging.MaxTopic;
import com.max.web.model.HandlerResults;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test subscribe and unsubscribe
 */
//@Ignore // Actually runs subscription and stuff, so only use it when you know what you're doing
public class UserActivityTopicServicesControllerTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    UserActivityTopicServicesController services;

    @Test
    public void testSubscribe() throws Exception
    {
        String subscriberName = "TaskEngine";
        RemoteSubscriber subscriber = new RemoteSubscriber();
        subscriber.setName(subscriberName);
        subscriber.setRestUrl("http://echo.jsontest.com/message/Hi/success/true");
        subscriber.setAutoRegister(true);
        subscriber.setTopic(MaxTopic.DataIntegrity);

        final HandlerResults subscribeResults = services.subscribe("", "", "", subscriber);

        assertThat(subscribeResults.isSuccess()).isTrue();

        services.unsubscribe("", "", "", subscriberName);
    }

    @Test
    public void testUnsubscribe() throws Exception
    {
        services.unsubscribe("", "", "", "DefaultTester");
    }
}
