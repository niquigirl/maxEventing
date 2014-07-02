package com.max.web.controller;

import com.max.db.dao.RemoteSubscriberDao;
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
@Component("SeeSubscribersServlet")
public class SeeSubscribersServlet implements HttpRequestHandler
{
    Logger log = Logger.getLogger(SeeSubscribersServlet.class);

    @Autowired
    RemoteSubscriberDao remoteSubscriberDao;

    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        log.debug("Just hit See Subscribers");
        log.debug("Found stuff: " + remoteSubscriberDao.findAll().toString());
    }
}
