package com.max.messaging.listeners;

import com.max.BaseMockUnitTest;
import org.junit.Test;

import javax.jms.MessageConsumer;

/**
 * Validate subscribing to the mail Queue
 */
public class QueueSubscriberTest extends BaseMockUnitTest
{
    @Test
    public void testReceiveMessages() throws Exception
    {
        QueueSubscriber queueReceiver = new QueueSubscriber();
        MessageConsumer consumer = queueReceiver.registerSubscriber("testQueue");
        queueReceiver.receiveMessages(consumer);
    }

}
