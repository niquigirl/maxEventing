package com.max.web.controller;

import com.max.messaging.MaxTopic;
import com.max.messaging.subscribe.TopicManagementException;
import com.max.services.InvalidSubscriberException;
import com.max.services.QueueManager;
import com.max.web.model.HandlerResults;
import com.max.web.model.RemoteSubscription;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Web Service for publishing messages
 */
@Controller
public class UserActivityTopicServicesController
{
    Logger log = Logger.getLogger(UserActivityTopicServicesController.class);

    @Autowired
    QueueManager queueManager;

    /**
     * Log and return a simple String message
     *
     * @return {@code String} An arbitrary message
     */
    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public String test()
    {
        log.debug("Just hit the Test service !");
        return "Just hit the Test service!";
    }


    /**
     * Log and return a simple String message
     *
     * @return {@code String} An arbitrary message
     */
    @RequestMapping(value = "testDIHandler", method = RequestMethod.POST)
    @ResponseBody
    public HandlerResults testDI(@NotNull @RequestBody String message)
    {
        log.debug("Test DI service hit: " + message);
        return new HandlerResults("Test DI Handler hit: " + message, true);
    }

    /**
     * Log and return a simple String message
     *
     * @return {@code String} An arbitrary message
     */
    @RequestMapping(value = "testUAHandler", method = RequestMethod.POST)
    @ResponseBody
    public HandlerResults testUA(@NotNull @RequestBody String message)
    {
        log.debug("Test UA service hit: " + message);
        return new HandlerResults("Test UA Handler hit: " + message, true);
    }


    /**
     * Publish a message to the queue
     *
     * @param version {@code String}
     * @param lang    {@code String}
     * @param country {@code String}
     * @param message {@link com.max.web.model.DefaultActivityMessage}
     * @return {@link com.max.web.model.HandlerResults}
     * @throws Exception
     */
    @RequestMapping(value = "{version}/{lang}/{country}/{topic}/publish", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unused")
    public HandlerResults publish(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                  @PathVariable("topic") MaxTopic topic,
                                  @NotNull @RequestBody String message) throws Exception
    {
        log.debug("Servicing request to publish a message to topic: " + topic + " :  " + message);

        try
        {
            queueManager.sendMessage(topic, message);
        }
        catch (Exception e)
        {
            return new HandlerResults("Failure on publish message : " + e.getMessage(), false);
        }

        return new HandlerResults("Message was published", true);
    }

    /**
     * Subscribe to user activity messages
     *
     * @param version    {@code String}
     * @param lang       {@code String}
     * @param country    {@code String}
     * @param subscriber {@link com.max.db.model.RemoteSubscriber}
     * @return {@link com.max.web.model.HandlerResults}
     * @throws Exception
     */
    @RequestMapping(value = "{version}/{lang}/{country}/subscribe", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unused")
    public HandlerResults subscribe(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                    @NotNull @RequestBody RemoteSubscription subscriber) throws Exception
    {
        HandlerResults results = new HandlerResults();

        try
        {
            queueManager.register(subscriber);
            results.setMessage("Subscriber " + subscriber.getName() + " Registered");
            results.setSuccess(true);
        }
        catch (InvalidSubscriberException e)
        {
            results.setMessage(e.getMessage());
            results.setSuccess(false);
        }

        return results;
    }

    /**
     * Unsubscribe from user activity messages
     *
     * @param version    {@code String}
     * @param lang       {@code String}
     * @param country    {@code String}
     * @param subscriber {@link com.max.db.model.RemoteSubscriber}
     * @return {@link com.max.web.model.HandlerResults}
     * @throws Exception
     */
    @RequestMapping(value = "{version}/{lang}/{country}/{topic}/unsubscribe", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unused")
    public HandlerResults unsubscribe(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                      @NotNull @PathVariable MaxTopic topic,
                                      @NotNull @RequestParam String subscriber) throws Exception
    {
        HandlerResults results = new HandlerResults();

        try
        {
            queueManager.unregister(topic, subscriber);
            results.setMessage("Subscriber " + subscriber + " unregistered");
            results.setSuccess(true);
        }
        catch (TopicManagementException e)
        {
            results.setMessage(e.getMessage());
            results.setSuccess(false);
        }

        return results;
    }


}
