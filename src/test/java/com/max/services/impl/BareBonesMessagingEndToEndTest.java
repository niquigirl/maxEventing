package com.max.services.impl;

import com.max.BaseMockUnitTest;
import com.max.db.dao.RemoteSubscriberDao;
import com.max.messaging.TopicSettings;
import com.max.messaging.publish.InvalidMessageException;
import com.max.messaging.subscribe.SubscriptionDetails;
import com.max.messaging.subscribe.TopicManagementException;
import com.max.messaging.wso2.WSO2DurableTopicSubscriber;
import com.max.services.InvalidSubscriberException;
import com.max.web.model.DefaultActivityMessage;
import org.junit.Test;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;

import static org.mockito.Mockito.mock;

/**
 * Tests Bare-bones end-to-end messaging - registering a subscriber, sending/receiving a message.
 * Should use to validate property setting (e.g. filterable properties such as verb) and filter strings
 */
public class BareBonesMessagingEndToEndTest extends BaseMockUnitTest
{

    public static final String SUBSCRIBER_NAME = "StandAloneTester";
    public static final String TESTER_SERVICE_URL = "http://echo.jsontest.com/message/Hi/success/true";
    public static final String TOPIC_NAME = "StandaloneTesting";
    public static final String TEST_VERB_1 = "Verb1";
    public static final String TEST_VERB_2 = "Verb2";
    public static final String TEST_SUBJECT_TYPE = "TestSubjectType";

    @Test
    public void validateEndToEnd() throws TopicManagementException, IOException, JMSException, InvalidMessageException, NamingException, InvalidSubscriberException
    {
        WSO2DurableTopicSubscriber subscriber = new WSO2DurableTopicSubscriber();

        final TopicSettings settings = new TopicSettings();
        settings.setCarbonClientId("carbon");
//        settings.setCarbonDefaultHostname("localhost");
        settings.setCarbonDefaultHostname("maxbussvcs.com");
        settings.setCarbonDefaultPort("5672");
        settings.setCarbonVirtualHostName("carbon");
        settings.setConnectionFactoryName("andesConnectionfactory");
        settings.setConnectionFactoryNamePrefix("connectionfactory.");
        settings.setPassword("admin");
        settings.setQpidIcf("org.wso2.andes.jndi.PropertiesFileInitialContextFactory");
        settings.setTopicName(TOPIC_NAME);
        settings.setTopicNamePrefix("topic.");
        settings.setUserName("admin");
        final SubscriptionDetails details = new SubscriptionDetails();
        details.setFilterString("verb='" + TEST_VERB_2 + "'");

        final RemoteSubscriberFacade listener = new RemoteSubscriberFacade();
        listener.setName(SUBSCRIBER_NAME);
        listener.setServiceUrl(TESTER_SERVICE_URL);
        details.setListener(listener);
        details.setSubscriberName(SUBSCRIBER_NAME);

        subscriber.register(settings, details);

        DefaultActivityMessage message = new DefaultActivityMessage();
        message.setVerb(TEST_VERB_1);
        final DefaultActivityMessage.Subject subject = new DefaultActivityMessage.Subject();
        subject.setObjectType(TEST_SUBJECT_TYPE);
        message.setObject(subject);
        new ActivityQueueManager(mock(RemoteSubscriberDao.class), false).sendMessage(settings, message.toString());

        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            // Short hair, don't care
        }
    }
}
