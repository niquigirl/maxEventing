package com.max.messaging.listeners;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import javax.validation.constraints.NotNull;

/**
 * Message Publisher - makes messages available for web pages listening
 */
public class WebSubscriber
{
    private static WebSubscriber publisher;

    private WebSubscriber()
    {
    }

    public static WebSubscriber getInstance()
    {
        if (publisher == null)
            publisher = new WebSubscriber();

        return publisher;
    }

    public boolean subscribe(final @NotNull String channel, final String message) throws PubnubException
    {
        Pubnub pubnub = new Pubnub("pub-c-02723478-99a6-4f63-9d74-f82e11034f59", "sub-c-cd455cbc-a4c2-11e3-a0b1-02ee2ddab7fe");
        Callback callback = new Callback()
        {
            public void successCallback(String channel, Object response)
            {
                System.out.println(response.toString());
            }

            public void errorCallback(String channel, PubnubError error)
            {
                System.out.println(error.toString());
            }
        };

        pubnub.subscribe(channel, callback);

        return true;
    }
}
