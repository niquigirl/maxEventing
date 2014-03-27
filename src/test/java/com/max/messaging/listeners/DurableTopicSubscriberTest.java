package com.max.messaging.listeners;

import com.max.BaseMockUnitTest;
import com.max.messaging.listeners.impl.NotificationSubscriber;
import org.junit.Test;

import java.util.Properties;


/**
 * Validate subscribing to the mail Topic
 */
public class DurableTopicSubscriberTest extends BaseMockUnitTest
{
    @Test
    public void testReceiveMessages() throws Exception
    {
        Properties filters = new Properties();
//        filters.put("verb", "NewAssociate");
        DurableTopicSubscriber queueReceiver = new NotificationSubscriber("Test2Topic", filters);

        while (true)
        {
            Thread.sleep(1000);
        }
    }

}
