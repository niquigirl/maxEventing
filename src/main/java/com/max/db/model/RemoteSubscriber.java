package com.max.db.model;

import com.max.messaging.MaxTopic;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity bean for remote subscribers
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "RemoteSubscriber")
public class RemoteSubscriber implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    private String name;
    private String restUrl;
    private String filterString;
    @Enumerated(EnumType.STRING)
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

    public boolean getAutoRegister()
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
}
