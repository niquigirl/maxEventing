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
    @JoinColumn(name = "resultTaskTemplateId", referencedColumnName = "id")
    private TaskTemplate taskToSpin;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "triggerTaskTemplateId", referencedColumnName = "id")
    private TaskTemplate triggerTask;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "dependentTaskTemplateId", referencedColumnName = "id")
    private TaskTemplate dependentTask;

    private Integer numberToComplete;
    private Integer autoDueDateNumDays;
    @Enumerated(EnumType.STRING)
    private ASSIGNEE_TYPE assigneeType;
    @Enumerated(EnumType.STRING)
    private SUBJECT_TYPE subjectType;
    private Integer minRepeatDelayNumDays;
    @Basic
    @Column(name = "canRepeat", columnDefinition = "BIT", length = 1)
    private boolean canRepeat;

    public static enum ASSIGNEE_TYPE
    {
        ASSOCIATE, ENROLLER, SPONSOR, UPLINE_BRONZE, UPLINE_SILVER, UPLINE_GOLD
    }

    public static enum SUBJECT_TYPE
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

    public TaskTemplate getTriggerTask()
    {
        return triggerTask;
    }

    public void setTriggerTask(TaskTemplate triggerTask)
    {
        this.triggerTask = triggerTask;
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
