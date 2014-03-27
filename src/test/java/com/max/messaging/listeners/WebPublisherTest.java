package com.max.messaging.listeners;

import com.max.BaseMockUnitTest;
import org.junit.Test;

/**
 * Created by neastman on 3/6/14.
 */
public class WebPublisherTest extends BaseMockUnitTest
{
    @Test
    public void testPublishMessage() throws Exception
    {
        WebPublisher.getInstance().publishMessage("root", "{\"message\":\"say it!\"}");

        try
        {
            Thread.sleep(2000);
        }
        catch (Exception e)
        {
            // Short hair, don't care
        }
    }
}
