package com.max.web.controller;

import com.max.services.QueueManager;
import com.max.web.model.HandlerResults;
import com.max.web.model.RemoteSubscription;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
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
@Component("SubscribeServlet")
public class SubscribeServlet implements HttpRequestHandler
{
    Logger log = Logger.getLogger(SubscribeServlet.class);

    @Autowired
    QueueManager queueManager;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        log.info("Handling request to subscribe");
        Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
        String body = s.hasNext() ? s.next() : "";

        ObjectMapper mapper = new ObjectMapper();
        final ObjectReader reader = mapper.reader(RemoteSubscription.class);

        RemoteSubscription subscription = reader.readValue(body);
        log.debug("Servicing request to subscribe to topic: " + subscription.getTopic() + " : name: " + subscription.getName() + " rest url " + subscription.getRestUrl());

        HandlerResults results;
        try
        {
            queueManager.register(subscription);
            results = new HandlerResults("Subscriber was registered", true);
        }
        catch (Exception e)
        {
            log.error("Something happened trying to register a subscriber: " + e.getMessage());
            results = new HandlerResults("Failure on register subscriber: " + e.getMessage(), false);
        }

        httpServletResponse.setContentType("application/json; charset=utf8");
        httpServletResponse.getWriter().write(results.toString());
    }
}
