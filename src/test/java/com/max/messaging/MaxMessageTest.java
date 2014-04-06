package com.max.messaging;

import com.max.messaging.message.MaxMessage;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test
 */
public class MaxMessageTest
{

    @Test
    public void validateSimpleUnmarshal()
    {
        String myJson = "{\"verb\":\"Doing Something\", \"language\":\"en\", \"actor\":{\"id\":\"123\"}}";

        try
        {
            MaxMessage result = MaxMessage.getInstance(myJson);

            assertThat(result).isNotNull();
            assertThat(result.getVerb()).isEqualTo("Doing Something");
            assertThat(result.getLanguage()).isEqualTo("en");
            assertThat(result.getActor()).isNotNull();
            assertThat(result.getActor().getId()).isEqualTo(123);

            System.out.println("Asserted super basic things about unmarshalling a MaxMessage from JSON");
        }
        catch (JSONException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
