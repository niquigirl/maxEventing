package com.max.coaching.db.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity bean for remote subscribers
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "RemoteSubscriptionCriteria")
public class RemoteSubscriptionCriteria extends CoachingMaxElement
{
    @ManyToOne
    @JoinColumn(name = "remoteSubscriberId", referencedColumnName = "id")
    private RemoteSubscriber remoteSubscriber;

    private String verb;
    private String subjectType;
    private String actorType;

    public RemoteSubscriber getRemoteSubscriber()
    {
        return remoteSubscriber;
    }

    public void setRemoteSubscriber(RemoteSubscriber remoteSubscriber)
    {
        this.remoteSubscriber = remoteSubscriber;
    }

    public String getVerb()
    {
        return verb;
    }

    public void setVerb(String verb)
    {
        this.verb = verb;
    }

    public String getSubjectType()
    {
        return subjectType;
    }

    public void setSubjectType(String subjectType)
    {
        this.subjectType = subjectType;
    }

    public String getActorType()
    {
        return actorType;
    }

    public void setActorType(String actorType)
    {
        this.actorType = actorType;
    }
}
