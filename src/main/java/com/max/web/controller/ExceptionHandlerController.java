package com.max.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Web Service for publishing messages
 */
@Controller
public class ExceptionHandlerController
{

    Logger log = Logger.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest request, Exception exception)
    {
        UUID errorId = UUID.randomUUID();

        final String message = exception.getMessage() + " [errorId " + errorId + "] : " + request.getRequestURL();
        log.error(message, exception);

        return message;
    }

}
