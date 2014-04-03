package com.max.web.services;

import com.max.messaging.message.MaxMessage;
import com.max.messaging.publish.TopicPublisher;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    //
    // Now, for the intentions of THIS servlet
    //

    @RequestMapping(value = "publish", method = RequestMethod.POST)
    public void publish(MaxMessage message) throws ServletException, IOException
    {
        log.debug("Servicing request to publish a message!!  :  " + message);

        try
        {
            userActivityTopicPublisher.sendMessages(userActivityTopicPublisher.getTopicSettings().getTopicName(), message); // TODO
        }
        catch (NamingException | JMSException e)
        {
            e.printStackTrace();
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
