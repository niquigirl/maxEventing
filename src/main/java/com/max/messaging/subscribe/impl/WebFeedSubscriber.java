package com.max.messaging.subscribe.impl;

import com.max.messaging.MessageUtils;
import com.max.messaging.message.MaxMessage;
import com.max.messaging.subscribe.DurableTopicSubscriber;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.Serializable;


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
        System.out.println("Starting WebFeedSubscriber onMessage");
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


        WebFeedMessage webMessage = new WebFeedMessage();
        webMessage.setWebMessage(MessageUtils.getWebFeedMessage(message.getVerb(), message.getLanguage()));

        JSONObject webMessageJson = new JSONObject(webMessage);
        pubnub.publish(getWebChannel(), webMessageJson, callback);
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

    public static class WebFeedMessage implements Serializable
    {
        public String webMessage;

        public String getWebMessage()
        {
            return webMessage;
        }

        public void setWebMessage(String webMessage)
        {
            this.webMessage = webMessage;
        }
    }
}
