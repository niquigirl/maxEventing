package com.max.web.controller;

import com.max.coaching.db.model.RemoteSubscriber;
import com.max.messaging.TopicSettings;
import com.max.messaging.publish.UserActivityTopicPublisher;
import com.max.messaging.subscribe.TopicManagementException;
import com.max.services.InvalidSubscriberException;
import com.max.services.SubscriberManager;
import com.max.web.model.HandlerResults;
import com.max.web.model.MaxMessage;
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
    UserActivityTopicPublisher userActivityUserActivityTopicPublisher;

    @Autowired
    TopicSettings userActivityTopicSettings;

    @Autowired
    SubscriberManager userActivitySubscriberManager;

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
     * Publish a message to the queue
     *
     * @param version {@code String}
     * @param lang    {@code String}
     * @param country {@code String}
     * @param message {@link com.max.web.model.MaxMessage}
     * @return {@link com.max.web.model.HandlerResults}
     * @throws Exception
     */
    @RequestMapping(value = "{version}/{lang}/{country}/publish", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unused")
    public HandlerResults publish(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                  @NotNull @RequestBody MaxMessage message) throws Exception
    {
        log.debug("Servicing request to publish a message!!  :  " + message);

        try
        {
            userActivityUserActivityTopicPublisher.sendMessage(message);
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
     * @param subscriber {@link com.max.coaching.db.model.RemoteSubscriber}
     * @return {@link com.max.web.model.HandlerResults}
     * @throws Exception
     */
    @RequestMapping(value = "{version}/{lang}/{country}/subscribe", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unused")
    public HandlerResults subscribe(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                    @NotNull @RequestBody RemoteSubscriber subscriber) throws Exception
    {
        HandlerResults results = new HandlerResults();

        try
        {
            userActivitySubscriberManager.register(subscriber);
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
     * @param subscriber {@link com.max.coaching.db.model.RemoteSubscriber}
     * @return {@link com.max.web.model.HandlerResults}
     * @throws Exception
     */
    @RequestMapping(value = "{version}/{lang}/{country}/unsubscribe", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unused")
    public HandlerResults unsubscribe(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                      @NotNull @RequestParam String subscriber) throws Exception
    {
        HandlerResults results = new HandlerResults();

        try
        {
            userActivitySubscriberManager.unregister(subscriber);
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
