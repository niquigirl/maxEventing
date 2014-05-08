package com.max.messaging;

import com.max.messaging.message.MaxMessage;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test
 */
public class MaxMessageTest
{

    @Test
    public void validateSimpleMarshal()
    {
        MaxMessage message = new MaxMessage();
        final MaxMessage.Subject subject = new MaxMessage.Subject();
        subject.setId(123);
        subject.setObjectType("Order");
        final MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setObjectType("User");
        actor.setObjectSubtype("Associate");
        actor.setId(555);
        final MaxMessage.Location location = new MaxMessage.Location();
        location.setLat("40.6193");
        location.setLng("-111.7895");

        message.setActor(actor);
        message.setLocation(location);
        message.setObject(subject);
        message.setVerb("Placed");
        message.setPublished(new Date());

        System.out.println(message.toString());
    }
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

    @Test
    public void validateSimpleUnmarshal_knownFail()
    {
        String myJson = "{\"verb\":\"LoggedIn\", \"actor\": {\"id\":\"513683\", \"objectType\":\"undefined\"}, \"object\": {\"id\":\"\", \"objectType\":\"undefined\"}}";

        try
        {
            MaxMessage result = MaxMessage.getInstance(myJson);

            assertThat(result).isNotNull();
            assertThat(result.getVerb()).isEqualTo("LoggedIn");
            assertThat(result.getLanguage()).isEqualTo("en");
            assertThat(result.getActor()).isNotNull();
            assertThat(result.getActor().getId()).isEqualTo(513683);

            System.out.println("Asserted super basic things about unmarshalling a MaxMessage from JSON");
        }
        catch (JSONException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
