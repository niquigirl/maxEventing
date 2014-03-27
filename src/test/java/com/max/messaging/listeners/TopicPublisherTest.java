package com.max.messaging.listeners;

import com.max.BaseMockUnitTest;
import org.junit.Test;

/**
 * Validate QueueSender
 */
public class TopicPublisherTest extends BaseMockUnitTest
{
    @Test
    public void testSendMessages() throws Exception
    {
        TopicPublisher topicPublisher = TopicPublisher.getInstance();

        topicPublisher.sendMessages("TesterTopic", "{\"verb\":\"NewAssociate\", \"foo\":\"fooValue\"}");
    }
}
