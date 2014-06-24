package com.max.messaging;

import com.max.web.model.DefaultActivityMessage;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test
 */
public class DefaultActivityMessageTest
{

/*
    @Test
    public void validateSimpleMarshal()
    {
        DefaultActivityMessage message = new DefaultActivityMessage();
        final DefaultActivityMessage.Subject subject = new MaxMessage.Subject();
        subject.setObjectType("Order");
        final DefaultActivityMessage.Actor actor = new DefaultActivityMessage.Actor();
        actor.setObjectType("User");
        actor.setObjectSubtype("Associate");
        final DefaultActivityMessage.Location location = new DefaultActivityMessage.Location();
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
            DefaultActivityMessage result = MaxMessage.getInstance(myJson);

            assertThat(result).isNotNull();
            assertThat(result.getVerb()).isEqualTo("Doing Something");
            assertThat(result.getLanguage()).isEqualTo("en");
            assertThat(result.getActor()).isNotNull();

            System.out.println("Asserted super basic things about unmarshalling a ActivityMessage from JSON");
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
            DefaultActivityMessage result = DefaultActivityMessage.getInstance(myJson);

            assertThat(result).isNotNull();
            assertThat(result.getVerb()).isEqualTo("LoggedIn");
            assertThat(result.getLanguage()).isEqualTo("en");
            assertThat(result.getActor()).isNotNull();

            System.out.println("Asserted super basic things about unmarshalling a ActivityMessage from JSON");
        }
        catch (JSONException | IOException e)
        {
            e.printStackTrace();
        }
    }
*/
}
