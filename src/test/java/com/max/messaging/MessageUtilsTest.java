package com.max.messaging;

import com.max.BaseMockUnitTest;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Validate MessageUtils behavior
 */
public class MessageUtilsTest extends BaseMockUnitTest
{
    @Test
    public void testGetTaskDescription() throws Exception
    {
        String message = MessageUtils.getTaskDescription("LOG_IN", "en");

        assertThat(message).isNotNull();

        System.out.println(message);
    }

    @Test
    public void testGetNotificationMessage() throws Exception
    {
        String message = MessageUtils.getNotificationMessage("LoggedIn", "en");

        assertThat(message).isNotNull();

        System.out.println(message);
    }

    @Test
    public void testGetWebMessage() throws Exception
    {
        String message = MessageUtils.getWebFeedMessage("LoggedIn", "en");

        assertThat(message).isNotNull();

        System.out.println(message);
    }
}
