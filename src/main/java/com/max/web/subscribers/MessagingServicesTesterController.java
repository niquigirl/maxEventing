package com.max.web.subscribers;

import com.max.web.model.HandlerResults;
import com.max.web.model.MaxMessage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Controller to provide test services
 */
@Controller
public class MessagingServicesTesterController
{

    Logger log = Logger.getLogger(MessagingServicesTesterController.class);
    /**
     * <p><br/>
     * RequestMapping(value = "testRest", method = RequestMethod.POST) -- e.g.:<br/><br/>
     * {@code /secure/testRest}<br/>
     * </p>
     * <p>
     * A simple rest service to validate subscribers<br/>
     * </p>
     *
     * @return {@code String}
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "testRest", method = RequestMethod.POST)
    @ResponseBody
    public HandlerResults testRest(@NotNull @RequestBody MaxMessage message)
    {
        log.info("Running MessagingServicesTesterController.testRest : " + message);
        final String message1 = "Thanks for calling, the message you left was: " + message;
        Logger.getLogger(MessagingServicesTesterController.class).info(message1);

        HandlerResults results = new HandlerResults();
        results.setMessage("Remote handler executed");
        results.setSuccess(true);
        return results;
    }

    @SuppressWarnings("unused")
    @RequestMapping(value = "testRestForm", method = RequestMethod.POST)
    @ResponseBody
    public String testRestForm(@NotNull @ModelAttribute MaxMessage message)
    {
        log.info("Running MessagingServicesTesterController.testForm : " + message);
        final String message1 = "Thanks for calling from a form, the message you left was: " + message;
        Logger.getLogger(MessagingServicesTesterController.class).info(message1);

        return message1;
    }

    @SuppressWarnings("unused")
    @RequestMapping(value = "testGet", method = RequestMethod.GET)
    @ResponseBody
    public String testGet()
    {
        log.info("Running MessagingServicesTesterController.testGet");
        final String message1 = "Hello, who is it?";
        Logger.getLogger(MessagingServicesTesterController.class).info(message1);

        return message1;
    }
}
