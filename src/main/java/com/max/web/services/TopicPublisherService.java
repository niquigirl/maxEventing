package com.max.web.services;

import com.max.messaging.message.MaxMessage;
import com.max.messaging.publish.TopicPublisher;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Web Service for publishing messages
 */
@Controller
public class TopicPublisherService
{
    private String topicName;

    Logger log = Logger.getLogger(TopicPublisherService.class);

    @Autowired
    TopicPublisher userActivityTopicPublisher;

    @RequestMapping(value = "test", method=RequestMethod.GET)
    public @ResponseBody String test()
    {
        log.debug("Just hit the Test service!");
        return "Just hit the Test service!";
    }

    @RequestMapping(value = "publish", method = RequestMethod.POST)
    public @ResponseBody String publish(@RequestBody MaxMessage message) throws ServletException, IOException
    {
        log.debug("Servicing request to publish a message!!  :  " + message);

        try
        {
            userActivityTopicPublisher.sendMessage(userActivityTopicPublisher.getTopicSettings().getTopicName(), message); // TODO

            return "Message was published";
        }
        catch (NamingException | JMSException e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String getTopicName()
    {
        return topicName;
    }

    public void setTopicName(String topicName)
    {
        this.topicName = topicName;
    }
}
