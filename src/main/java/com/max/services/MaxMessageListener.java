package com.max.services;

import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;
import org.wso2.andes.client.message.JMSTextMessage;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * This should be extended by any listener, which takes the generic JMS stuff and casts it to
 * Max-specific stuff
 */
public abstract class MaxMessageListener implements MessageListener
{
    Logger log = Logger.getLogger(MaxMessageListener.class);

    public abstract HandlerResults onMessage(JMSTextMessage activityMessage);

    private String name;

    /**
     * Entry point for a Message.  This method casts the Message to the domain-specific type to be handled
     *
     * @param message {@code Message}
     */
    final public void onMessage(Message message)
    {
        System.out.println("*********************** Responding::: Starting onMessage " + name);
        final HandlerResults handlerResults = onMessage((JMSTextMessage) message);

        log.info("Handler " + getName() + " results: " + handlerResults);
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
