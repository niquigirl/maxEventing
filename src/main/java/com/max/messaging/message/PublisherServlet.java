package com.max.messaging.message;

import com.max.messaging.listeners.TopicPublisher;
import com.max.messaging.listeners.WebPublisher;
import com.max.messaging.listeners.impl.NotificationSubscriber;
import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;


/**
 * Handler for login request
 * Created by neastman on 1/13/14.
 */
public class PublisherServlet extends HttpServlet
{
    public static final String TOPIC_NAME = "TesterTopic";
    public static final String WEB_TOPIC = "root";
    Logger log = Logger.getLogger(PublisherServlet.class);

    /**
     * Subscribers and stuff; to be somewhere else someday
     */
    private static final Properties filters;
    public static final NotificationSubscriber notificationSubscriber;

    static
    {
        filters = new Properties();
        filters.put("verb", "NewAssociate");
        notificationSubscriber = new NotificationSubscriber(TOPIC_NAME, filters);
    }

    @Override
    public void destroy()
    {
        try
        {
            System.out.println("KILLING NOTIFICATION SUBSCRIBER!!!!!!!!");
            notificationSubscriber.getTopicSession().unsubscribe(notificationSubscriber.getSubscriptionId());
            notificationSubscriber.doFinalize();

            Thread.sleep(5000);
        }
        catch (JMSException | InterruptedException e)
        {
            e.printStackTrace();
        }
        super.destroy();
    }

    //
    // Now, for the intentions of THIS servlet
    //

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        String requestData = getRequestData(req);
        log.debug("Servicing request to publish a message!!  :  " + requestData);

        // Send to Web messages
        TopicPublisher sender = TopicPublisher.getInstance();

        try
        {
            sender.sendMessages(TOPIC_NAME, requestData); // TODO
        }
        catch (NamingException | JMSException e)
        {
            e.printStackTrace();
        }

        WebPublisher webPublisher = WebPublisher.getInstance();
        webPublisher.publishMessage(WEB_TOPIC, requestData);
    }

    private String getRequestData(HttpServletRequest req)
    {
        StringBuilder jb = new StringBuilder();
        String line;

        try
        {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        }
        catch (Exception e)
        {
            // todo
        }

        return jb.toString();
    }
}
