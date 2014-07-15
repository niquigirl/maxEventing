package com.max.web.controller;

import com.max.messaging.MaxTopic;
import com.max.services.QueueManager;
import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

/**
 * Expose a simple service
 */
@Component("PublishMessageServlet")
public class PublishMessageServlet implements HttpRequestHandler
{
    Logger log = Logger.getLogger(PublishMessageServlet.class);

    @Autowired
    QueueManager queueManager;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        final StringBuffer requestURL = request.getRequestURL();
        String topic = requestURL.substring(requestURL.lastIndexOf("/") + 1);

        Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
        String body = s.hasNext() ? s.next() : "";

        log.debug("Servicing request to publish a message to topic: " + topic + ":  " + body);

        HandlerResults results;
        try
        {
            queueManager.sendMessage(MaxTopic.valueOf(topic), body);
            results = new HandlerResults("Message was published", true);
        }
        catch (Exception e)
        {
            log.error("Something happened trying to publish a message: " + e.getMessage());
            results = new HandlerResults("Failure on publish message : " + e.getMessage(), false);
        }

        httpServletResponse.setContentType("application/json; charset=utf8");
        httpServletResponse.getWriter().write(results.toString());
    }
}
