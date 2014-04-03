package com.max.messaging.subscribe.impl;

import com.max.messaging.message.MaxMessage;
import com.max.messaging.subscribe.DurableTopicSubscriber;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import org.apache.log4j.Logger;
import org.json.JSONObject;


/**
 * Topic subscriber which handles tasks related to notifications
 */
public class WebFeedSubscriber extends DurableTopicSubscriber
{
    private String webChannel;
    private String publishKey;
    private String subscribeKey;

    Logger log = Logger.getLogger(WebFeedSubscriber.class);

    @Override
    public void onMessage(MaxMessage message)
    {
        Pubnub pubnub = new Pubnub(getPublishKey(), getSubscribeKey());
        Callback callback = new Callback()
        {
            public void successCallback(String channel, Object response)
            {
                log.debug("WebFeedSubscriber - success publish callback - response: " + response.toString());
            }

            public void errorCallback(String channel, PubnubError error)
            {
                log.debug("WebFeedSubscriber - FAIL publish callback - response: " + error.toString());
            }
        };

        JSONObject foo = new JSONObject(message);
        pubnub.publish(getWebChannel(), foo, callback);
    }

    public String getWebChannel()
    {
        return webChannel;
    }

    public void setWebChannel(String webChannel)
    {
        this.webChannel = webChannel;
    }

    public String getPublishKey()
    {
        return publishKey;
    }

    public void setPublishKey(String publishKey)
    {
        this.publishKey = publishKey;
    }

    public String getSubscribeKey()
    {
        return subscribeKey;
    }

    public void setSubscribeKey(String subscribeKey)
    {
        this.subscribeKey = subscribeKey;
    }
}
