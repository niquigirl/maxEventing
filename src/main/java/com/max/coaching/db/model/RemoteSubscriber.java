package com.max.coaching.db.model;

import javax.persistence.*;
import java.util.List;

/**
 * Entity bean for remote subscribers
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "RemoteSubscriber")
public class RemoteSubscriber extends CoachingMaxElement
{
    @OneToMany(mappedBy = "remoteSubscriber", fetch = FetchType.EAGER)
    private List<RemoteSubscriptionCriteria> remoteSubscriptionCriteria;

    private String name;
    private String restUrl;
    private String testUrl;
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

    public String getTestUrl()
    {
        return testUrl;
    }

    public void setTestUrl(String testUrl)
    {
        this.testUrl = testUrl;
    }

    public Integer getTimeout()
    {
        return timeout;
    }

    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    public List<RemoteSubscriptionCriteria> getRemoteSubscriptionCriteria()
    {
        return remoteSubscriptionCriteria;
    }

    public void setRemoteSubscriptionCriteria(List<RemoteSubscriptionCriteria> remoteSubscriptionCriteria)
    {
        this.remoteSubscriptionCriteria = remoteSubscriptionCriteria;
    }

    public boolean isAutoRegister()
    {
        return autoRegister;
    }

    public void setAutoRegister(boolean autoRegister)
    {
        this.autoRegister = autoRegister;
    }
}
