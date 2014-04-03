package com.max.messaging;

import com.max.messaging.message.MaxMessage;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by neastman on 3/24/14.
 */
public class MaxMessageTest
{

    @Test
    public void validateSimpleUnmarshal()
    {
        String myJson = "{\"verb\":\"Doing Something\", \"language\":\"en\", \"actor\":{\"id\":\"id_123\"}}";

        try
        {
            MaxMessage result = MaxMessage.getInstance(myJson);

            System.out.println(result.getVerb());
        }
        catch (JSONException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
