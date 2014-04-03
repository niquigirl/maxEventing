package com.max.coaching.db.model;

import javax.persistence.*;

/**
 * Customer entity bean for MaxReporting (Exigo) customers
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "AutoTaskFlow")
public class AutoTaskFlow extends CoachingMaxElement
{
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "taskTemplateId", referencedColumnName = "id")
    private TaskTemplate taskToSpin;
    private String eventName;
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "dependentTaskTemplateId", referencedColumnName = "id")
    private TaskTemplate dependentTask;
    private Integer numberToComplete;
    private Integer autoDueDateNumDays;
    private ASSIGNEE_TYPE assigneeType;
    private SUBJECT_TYPE subjectType;
    private Integer minRepeatDelayNumDays;
    @Basic
    @Column(name = "canRepeat", columnDefinition = "BIT", length = 1)
    private boolean canRepeat;

    public enum ASSIGNEE_TYPE
    {
        ASSOCIATE, UPLINE_BRONZE, SPONSOR, UPLINE_GOLD, ENROLLER
    }

    public enum SUBJECT_TYPE
    {
        ASSOCIATE, PROSPECT, VIDEO
    }

    public TaskTemplate getTaskToSpin()
    {
        return taskToSpin;
    }

    public void setTaskToSpin(TaskTemplate taskToSpin)
    {
        this.taskToSpin = taskToSpin;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public TaskTemplate getDependentTask()
    {
        return dependentTask;
    }

    public void setDependentTask(TaskTemplate dependentTask)
    {
        this.dependentTask = dependentTask;
    }

    public Integer getNumberToComplete()
    {
        return numberToComplete;
    }

    public void setNumberToComplete(Integer numberToComplete)
    {
        this.numberToComplete = numberToComplete;
    }

    public Integer getAutoDueDateNumDays()
    {
        return autoDueDateNumDays;
    }

    public void setAutoDueDateNumDays(Integer autoDueDateNumDays)
    {
        this.autoDueDateNumDays = autoDueDateNumDays;
    }

    public ASSIGNEE_TYPE getAssigneeType()
    {
        return assigneeType;
    }

    public void setAssigneeType(ASSIGNEE_TYPE assigneeType)
    {
        this.assigneeType = assigneeType;
    }

    public Integer getMinRepeatDelayNumDays()
    {
        return (minRepeatDelayNumDays == null ? 0 : minRepeatDelayNumDays);
    }

    public void setMinRepeatDelayNumDays(Integer minRepeatDelayNumDays)
    {
        this.minRepeatDelayNumDays = minRepeatDelayNumDays;
    }

    public boolean isCanRepeat()
    {
        return canRepeat;
    }

    public void setCanRepeat(boolean canRepeat)
    {
        this.canRepeat = canRepeat;
    }

    public SUBJECT_TYPE getSubjectType()
    {
        return subjectType;
    }

    public void setSubjectType(SUBJECT_TYPE subjectType)
    {
        this.subjectType = subjectType;
    }
}
