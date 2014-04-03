package com.max.messaging;

import com.max.messaging.subscribe.QueueSubscriber;
import org.junit.Test;

import javax.jms.MessageConsumer;

/**
 * Validate subscribing to the mail Queue
 */
public class QueueSubscriberTest
{
    @Test
    public void testReceiveMessages() throws Exception
    {
        QueueSubscriber queueReceiver = new QueueSubscriber();
        MessageConsumer consumer = queueReceiver.registerSubscriber("testQueue");
        queueReceiver.receiveMessages(consumer);
    }

}
