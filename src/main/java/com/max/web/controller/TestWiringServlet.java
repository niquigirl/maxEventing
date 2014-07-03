package com.max.web.controller;

import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

/**
 * Expose a simple service
 */
public class TestWiringServlet extends HttpServlet
{
    Logger log = Logger.getLogger(TestWiringServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException
    {
        doPost(request, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException
    {
        log.info("This servlet works");

        Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
        String body = s.hasNext() ? s.next() : "";

        log.info("Received the following message: " + body);


        HandlerResults results = new HandlerResults("Tested, Approved!", true);
        resp.getWriter().write(results.toString());
    }
}
