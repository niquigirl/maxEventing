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

/**
 * Expose a simple service
 */
@Component("UnsubscribeServlet")
public class UnsubscribeServlet implements HttpRequestHandler
{
    Logger log = Logger.getLogger(UnsubscribeServlet.class);

    @Autowired
    QueueManager queueManager;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        final StringBuffer requestURL = request.getRequestURL();
        String topic = requestURL.substring(requestURL.lastIndexOf("/") + 1);

        String subscriberName = request.getParameter("subscriber");
        log.debug("Servicing request to unsubscribe from topic: " + topic + " : name: " + subscriberName);

        HandlerResults results;
        try
        {
            queueManager.unregister(MaxTopic.valueOf(topic), subscriberName);
            results = new HandlerResults("Subscriber was unregistered", true);
        }
        catch (Exception e)
        {
            log.error("Something happened trying to unsubscribe a subscriber: " + e.getMessage());
            results = new HandlerResults("Failure on unregister subscriber: " + e.getMessage(), false);
        }

        httpServletResponse.getWriter().write(results.toString());
    }
}
