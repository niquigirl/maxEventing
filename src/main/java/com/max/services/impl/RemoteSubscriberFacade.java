package com.max.services.impl;

import com.max.messaging.subscribe.MaxMessageListener;
import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.max.web.model.MaxMessage;

/**
 * Local subscriber serving as a facade for each registered remote subscriber
 */
@Service
@Scope("prototype")
public class RemoteSubscriberFacade extends MaxMessageListener
{
    private Logger log = Logger.getLogger(RemoteSubscriberFacade.class);

    private String serviceUrl;
    private String testUrl;
    private Integer timeoutMS;

    /**
     * <p>
     *     Call remote service on Message received
     * </p>
     *
     * @param maxMessage {@link com.max.web.model.MaxMessage}
     */
    @Override
    public HandlerResults onMessage(MaxMessage maxMessage)
    {
        getLogger().debug("Handling message from remote subscriber");
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(getTimeoutMS() == null ? 30 : getTimeoutMS());

        try
        {
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            final HandlerResults handlerResults = restTemplate.postForObject(getServiceUrl(), maxMessage, HandlerResults.class);
            final String message = "Received the following results from " + getServiceUrl() + " : " + handlerResults;
            getLogger().debug(message);
            return new HandlerResults(message, true);
        }
        catch (RestClientException e)
        {
            final String message = "Could not send message to remote subscriber URL " + getServiceUrl();
            getLogger().error(message, e);
            return new HandlerResults(message, false);
        }
    }

    /**
     * This method should execute some test stuff that proves connectivity is right and stuff.  Iit runs
     */
    @Override
    public HandlerResults onTest(MaxMessage testMessage)
    {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(getTimeoutMS() == null ? 30000 : getTimeoutMS());

        try
        {
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            final HandlerResults handlerResults = restTemplate.postForObject(getTestUrl(), testMessage, HandlerResults.class);
            getLogger().debug("Received the following results from " + getTestUrl() + " : " + handlerResults);
            return handlerResults;
        }
        catch (RestClientException e)
        {
            getLogger().error("Could not send message to remote subscriber " + getTestUrl(), e);
            return null;
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

    public String getTestUrl()
    {
        return testUrl;
    }

    public void setTestUrl(String testUrl)
    {
        this.testUrl = testUrl;
    }

    public Logger getLogger()
    {
        return log;
    }
}

