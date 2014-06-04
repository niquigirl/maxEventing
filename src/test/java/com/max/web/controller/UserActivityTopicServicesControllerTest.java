package com.max.web.controller;

import com.max.BaseSpringInjectionUnitTest;
import com.max.coaching.db.model.RemoteSubscriber;
import com.max.coaching.db.model.RemoteSubscriptionCriteria;
import com.max.web.model.HandlerResults;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test subscribe and unsubscribe
 */
@Ignore // Actually runs subscription and stuff, so only use it when you know what you're doing
public class UserActivityTopicServicesControllerTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    UserActivityTopicServicesController services;

    @Test
    public void testSubscribe() throws Exception
    {
        RemoteSubscriber subscriber = new RemoteSubscriber();
        subscriber.setName("TaskEngine");
        subscriber.setTestUrl("http://echo.jsontest.com/message/Hi/success/true");
        subscriber.setRestUrl("http://echo.jsontest.com/message/Hi/success/true");
        subscriber.setAutoRegister(true);

        final LinkedList<RemoteSubscriptionCriteria> remoteSubscriptionCriteria = new LinkedList<>();
        final RemoteSubscriptionCriteria criteria = new RemoteSubscriptionCriteria();
        criteria.setVerb("SomeVerb");
        criteria.setActorType("SomeActorType");
        remoteSubscriptionCriteria.add(criteria);
        final RemoteSubscriptionCriteria criteria2 = new RemoteSubscriptionCriteria();
        criteria2.setVerb("SomeOtherVerb");
        criteria2.setActorType("SomeOtherActorType");
        remoteSubscriptionCriteria.add(criteria2);
        subscriber.setRemoteSubscriptionCriteria(remoteSubscriptionCriteria);

        final HandlerResults subscribeResults = services.subscribe("", "", "", subscriber);

        assertThat(subscribeResults.isSuccess()).isTrue();
    }

    @Test
    public void testUnsubscribe() throws Exception
    {
        services.unsubscribe("", "", "", "TaskEngine");
    }
}
