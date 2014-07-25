package com.max.services.impl;

import com.max.BaseSpringInjectionUnitTest;
import com.max.messaging.MaxTopic;
import com.max.messaging.publish.InvalidMessageException;
import com.max.messaging.subscribe.TopicManagementException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;

/**
 *
 */
public class ActivityQueueManagerTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    ActivityQueueManager activitySubscriberManager;

    @Test
    public void sendSomething() throws IOException, JMSException, InvalidMessageException, NamingException
    {
        activitySubscriberManager.sendMessage(MaxTopic.DataIntegrity, "{\"verb\":\"AutoOrderCountryWarehouseCurrencyDIViolationFound\"}");
    }

    @Test
    public void testCached()
    {
        System.out.println(activitySubscriberManager.getCachedSubscribers());
    }

    @Test
    @Ignore
    public void testUnsubscribe() throws TopicManagementException
    {
        activitySubscriberManager.unregister(MaxTopic.DataIntegrity, "UnsubMe");
    }
}
