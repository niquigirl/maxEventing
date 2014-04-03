package com.max.coaching.db.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Customer entity bean for MaxReporting (Exigo) customers
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "AssociateTask")
public class AssociateTask extends CoachingMaxElement
{
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "taskTemplateId", referencedColumnName = "id")
    private TaskTemplate task;
    private Integer associateId;
    private Integer actorId;
    private Integer subjectId;
    private String subjectObjectType;
    @Temporal(TemporalType.DATE)
    private Date completedDate;
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @Basic
    @Column(name = "ignored", columnDefinition = "BIT", length = 1)
    private boolean ignored;

    public TaskTemplate getTask()
    {
        return task;
    }

    public void setTask(TaskTemplate task)
    {
        this.task = task;
    }

    public Integer getAssociateId()
    {
        return associateId;
    }

    public void setAssociateId(Integer associateId)
    {
        this.associateId = associateId;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Date getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }

    public boolean isIgnored()
    {
        return ignored;
    }

    public void setIgnored(boolean ignored)
    {
        this.ignored = ignored;
    }

    public Integer getActorId()
    {
        return actorId;
    }

    public void setActorId(Integer actorId)
    {
        this.actorId = actorId;
    }

    public Integer getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId)
    {
        this.subjectId = subjectId;
    }

    public Date getCompletedDate()
    {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate)
    {
        this.completedDate = completedDate;
    }

    public String getSubjectObjectType()
    {
        return subjectObjectType;
    }

    public void setSubjectObjectType(String subjectObjectType)
    {
        this.subjectObjectType = subjectObjectType;
    }
}
