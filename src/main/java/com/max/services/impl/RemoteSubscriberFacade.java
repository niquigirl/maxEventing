package com.max.services.impl;

import com.max.messaging.subscribe.MaxMessageListener;
import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.wso2.andes.client.message.JMSTextMessage;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Local subscriber serving as a facade for each registered remote subscriber
 */
@Service
@Scope("prototype")
public class RemoteSubscriberFacade extends MaxMessageListener
{
    private Logger log = Logger.getLogger(RemoteSubscriberFacade.class);

    private String serviceUrl;
    private Integer timeoutMS;

    /**
     * <p>
     * Call remote service on Message received
     * </p>
     *
     * @param activityMessage {@link com.max.web.model.DefaultActivityMessage}
     */
    @Override
    public HandlerResults onMessage(JMSTextMessage activityMessage)
    {
        getLogger().debug(this + " _ " + getName() + ": Handling message for remote subscriber");
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(getTimeoutMS() == null ? 30000 : getTimeoutMS());

        try
        {
            getLogger().debug("RemoteSubscriberFacade sending message: " + activityMessage.getText());
/*
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            final JSONObject request = new JSONObject(activityMessage.getText());
            getLogger().debug("JSONObject: " + request);
            final HandlerResults handlerResults = restTemplate.postForObject(getServiceUrl(), new JSONObject(activityMessage.getText()), HandlerResults.class);
*/
            final HandlerResults handlerResults = call(getServiceUrl(), activityMessage);
            final String message = getName() + ": Received the following results from " + getServiceUrl() + " : " + handlerResults;
            getLogger().debug(message);
            return new HandlerResults(message, true);
        }
        catch (RestClientException e)
        {
            final String message = "Could not send message to remote subscriber URL " + getServiceUrl() + " : " + e.getMessage();
            getLogger().error(message, e);
            return new HandlerResults(message, false);
        }
        catch (JMSException e)
        {
            final String message = "Could not get message text from message " + activityMessage + " : " + e.getMessage();
            getLogger().error(message, e);
            return new HandlerResults(message, false);
        }
    }

    private HandlerResults call(String serviceUrl, JMSTextMessage activityMessage)
    {
        try
        {
            URL url = new URL(serviceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
            conn.getOutputStream().write(activityMessage.getText().getBytes());

            if (conn.getResponseCode() != 200)
            {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            String result = "";
            while ((output = br.readLine()) != null)
            {
                result = result + output;
            }

            conn.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            final ObjectReader reader = mapper.reader(HandlerResults.class);

            return reader.readValue(result);
        }
        catch (IOException | JMSException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    public String getServiceUrl()
    {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl)
    {
        this.serviceUrl = serviceUrl;
    }

    public Integer getTimeoutMS()
    {
        return timeoutMS;
    }

    public void setTimeoutMS(Integer timeoutMS)
    {
        this.timeoutMS = timeoutMS;
    }

    public Logger getLogger()
    {
        return log;
    }

}

