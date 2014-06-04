package com.max.services.impl;

import com.max.BaseMockUnitTest;
import com.max.coaching.db.model.RemoteSubscriber;
import com.max.coaching.db.model.RemoteSubscriptionCriteria;
import org.junit.Test;

import java.util.LinkedList;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Mock and validate UserActivitySubscriberManager behavior
 */
public class UserActivitySubscriberManagerTest extends BaseMockUnitTest
{
    @Test
    public void testBuildFilterString()
    {
        RemoteSubscriber curSubscriber = new RemoteSubscriber();
        curSubscriber.setName("TestSubscriber");
        final LinkedList<RemoteSubscriptionCriteria> remoteSubscriptionCriteria = new LinkedList<>();

        final RemoteSubscriptionCriteria criteria = new RemoteSubscriptionCriteria();
        criteria.setVerb("SomeVerb");
        criteria.setActorType("SomeActorType");
        remoteSubscriptionCriteria.add(criteria);
        final RemoteSubscriptionCriteria criteria2 = new RemoteSubscriptionCriteria();
        criteria2.setVerb("SomeOtherVerb");
        criteria2.setActorType("SomeOtherActorType");
        remoteSubscriptionCriteria.add(criteria2);
        curSubscriber.setRemoteSubscriptionCriteria(remoteSubscriptionCriteria);

        final UserActivitySubscriberManager uarsff = new UserActivitySubscriberManager();

        final String filterString = uarsff.generateFilterString(curSubscriber);

        System.out.println(filterString);
        assertThat(filterString).isNotEmpty();
    }

    @Test
    public void registerAllRemoteSubscribers()
    {

    }
}
