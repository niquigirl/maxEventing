package com.max.services;

import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * This should be extended by any listener, which takes the generic JMS stuff and casts it to
 * Max-specific stuff
 */
public abstract class MaxMessageListener implements MessageListener
{
    Logger log = Logger.getLogger(MaxMessageListener.class);

    public abstract HandlerResults onMessage(String activityMessage);

    private String name;

    /**
     * Entry point for a Message.  This method casts the Message to the domain-specific type to be handled
     *
     * @param message {@code Message}
     */
    @Override
    final public void onMessage(Message message)
    {
        System.out.println("*********************** Responding::: Starting onMessage " + name);
        try
        {
            HandlerResults handlerResults;

            if (message instanceof TextMessage)
                handlerResults = onMessage(((TextMessage) message).getText());
            else
                handlerResults = new HandlerResults("Text Messages only!", false);

            log.info("Handler " + getName() + " results: " + handlerResults);
        }
        catch (JMSException e)
        {
            final String error = "Could not get message text from message " + message + " : " + e.getMessage();
            log.error(error, e);
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
