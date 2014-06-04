package com.max.messaging.subscribe;

import com.max.web.model.MaxMessage;
import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.wso2.andes.client.message.JMSTextMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.IOException;

/**
 * This should be extended by any listener, which takes the generic JMS stuff and casts it to
 * Max-specific stuff
 */
public abstract class MaxMessageListener implements MessageListener
{
    Logger log = Logger.getLogger(MaxMessageListener.class);

    public abstract HandlerResults onTest(MaxMessage maxMessage);
    public abstract HandlerResults onMessage(MaxMessage maxMessage);

    /**
     * Entry point for a Message.  This method casts the Message to the domain-specific type to be handled
     *
     * @param message {@code Message}
     */
    final public void onMessage(Message message)
    {
        System.out.println("Starting onMessage " + this);
        try
        {
            String messageText = ((JMSTextMessage) message).getText();

            try
            {
                MaxMessage maxMessage = MaxMessage.getInstance(messageText);
                onMessage(maxMessage);
            }
            catch (JSONException e)
            {
                log.error("MaxMessaging - Error - Could not generate a MaxMessage from the following<br/>" + messageText, e);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (JMSException e)
        {
            e.printStackTrace();
        }
    }
}
