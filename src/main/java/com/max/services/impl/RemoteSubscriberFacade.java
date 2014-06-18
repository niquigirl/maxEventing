package com.max.services.impl;

import com.max.messaging.subscribe.MaxMessageListener;
import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.wso2.andes.client.message.JMSTextMessage;

import javax.jms.JMSException;

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
     *     Call remote service on Message received
     * </p>
     *
     * @param activityMessage {@link com.max.web.model.DefaultActivityMessage}
     */
    @Override
    public HandlerResults onMessage(JMSTextMessage activityMessage)
    {
        getLogger().debug(getName() + ": Handling message from remote subscriber");
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(getTimeoutMS() == null ? 30 : getTimeoutMS());

        try
        {
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            final HandlerResults handlerResults = restTemplate.postForObject(getServiceUrl(), activityMessage.getText(), HandlerResults.class);
            final String message = getName() +": Received the following results from " + getServiceUrl() + " : " + handlerResults;
            getLogger().debug(message);
            return new HandlerResults(message, true);
        }
        catch (RestClientException e)
        {
            final String message = "Could not send message to remote subscriber URL " + getServiceUrl();
            getLogger().error(message, e);
            return new HandlerResults(message, false);
        }
        catch (JMSException e)
        {
            final String message = "Could not get message text from message " + activityMessage;
            getLogger().error(message, e);
            return new HandlerResults(message, false);
        }
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

