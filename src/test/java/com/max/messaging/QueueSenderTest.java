package com.max.messaging;

import com.max.messaging.publish.QueueSender;
import org.junit.Test;

/**
 * Validate QueueSender
 */
public class QueueSenderTest
{
    @Test
    public void testSendMessages() throws Exception
    {
        QueueSender queueSender = QueueSender.getInstance();
        queueSender.sendMessages("testQueue", "My name is niQuita chiQuita bonita...");
    }
}
