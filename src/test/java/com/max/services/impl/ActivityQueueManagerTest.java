package com.max.services.impl;

import com.max.BaseSpringInjectionUnitTest;
import com.max.messaging.MaxTopic;
import com.max.messaging.publish.InvalidMessageException;
import org.json.JSONException;
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
    public void sendSomething() throws IOException, JMSException, InvalidMessageException, JSONException, NamingException
    {
        activitySubscriberManager.sendMessage(MaxTopic.DataIntegrity, "{\"value\":5}");
    }
}
