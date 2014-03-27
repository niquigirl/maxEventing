package com.max.messaging.message;

import com.max.BaseMockUnitTest;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by neastman on 3/24/14.
 */
public class MaxMessageTest extends BaseMockUnitTest
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
