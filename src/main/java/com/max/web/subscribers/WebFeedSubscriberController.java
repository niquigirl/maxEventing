package com.max.web.subscribers;

import com.max.messaging.MessageUtils;
import com.max.web.model.HandlerResults;
import com.max.web.model.MaxMessage;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * Max Topic subscriber which handles tasks related to notifications
 */
@Controller
public class WebFeedSubscriberController
{

    Logger log = Logger.getLogger(WebFeedSubscriberController.class);

    @Autowired
    WebFeedProperties pubNubProperties;

    @SuppressWarnings("unused")
    @RequestMapping(value = "{version}/{lang}/{country}/testWebFeed", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public HandlerResults onTestMessage(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                              @NotNull @RequestBody MaxMessage message)
    {
        log.info("Running WebFeedSubscriberController.onTestMessage : " + message);
        HandlerResults results = new HandlerResults();
        results.setMessage(message.toString());
        results.setSuccess(true);

        return results;
    }

    @SuppressWarnings("unused")
    @RequestMapping(value = "{version}/{lang}/{country}/sendToWebFeed", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public HandlerResults onMessage(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                          @NotNull @RequestBody MaxMessage message)
    {
        log.info("Running WebFeedSubscriberController.onMessage : " + message);
        Pubnub pubnub = new Pubnub(pubNubProperties.getPublishKey(), pubNubProperties.getSubscribeKey());
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
        pubnub.publish(pubNubProperties.getWebChannel(), webMessageJson, callback);

        HandlerResults results = new HandlerResults();
        results.setSuccess(true);
        results.setMessage("WebFeedSubscriber complete");

        return results;
    }

    public static class WebFeedMessage implements Serializable
    {
        public String webMessage;

        @SuppressWarnings("unused") // marshalled as JSON
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
