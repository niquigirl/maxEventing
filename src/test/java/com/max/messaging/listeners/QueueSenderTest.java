package com.max.messaging.listeners;

import com.max.BaseMockUnitTest;
import org.junit.Test;

/**
 * Validate QueueSender
 */
public class QueueSenderTest extends BaseMockUnitTest
{
    @Test
    public void testSendMessages() throws Exception
    {
        QueueSender queueSender = QueueSender.getInstance();
        queueSender.sendMessages("testQueue", "My name is niQuita chiQuita bonita...");
    }
}
