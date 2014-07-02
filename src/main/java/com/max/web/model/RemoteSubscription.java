package com.max.web.model;

import com.max.db.model.RemoteSubscriber;
import com.max.messaging.MaxTopic;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Web bean for a RemoteSubscription
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteSubscription extends JsonData
{
    private String name;
    private String restUrl;
    private String filterString;
    private MaxTopic topic;
    private Integer timeout;
    private boolean autoRegister = true;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRestUrl()
    {
        return restUrl;
    }

    public void setRestUrl(String restUrl)
    {
        this.restUrl = restUrl;
    }

    public Integer getTimeout()
    {
        return timeout;
    }

    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    public boolean isAutoRegister()
    {
        return autoRegister;
    }

    public void setAutoRegister(boolean autoRegister)
    {
        this.autoRegister = autoRegister;
    }

    public String getFilterString()
    {
        return filterString;
    }

    public void setFilterString(String filterString)
    {
        this.filterString = filterString;
    }

    public MaxTopic getTopic()
    {
        return topic;
    }

    public void setTopic(MaxTopic topic)
    {
        this.topic = topic;
    }

    @JsonIgnore
    public RemoteSubscriber toData()
    {
        RemoteSubscriber data = new RemoteSubscriber();
        data.setAutoRegister(isAutoRegister());
        data.setFilterString(getFilterString());
        data.setTopic(getTopic());
        data.setName(getName());
        data.setRestUrl(getRestUrl());
        data.setTimeout(getTimeout());

        return data;
    }
}
