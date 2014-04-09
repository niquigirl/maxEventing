package com.max.messaging.subscribe.impl;

import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.model.AutoTaskFlow;
import com.max.coaching.db.repositories.AssociateTaskRepository;
import com.max.coaching.db.repositories.AutoTaskFlowRepository;
import com.max.coaching.db.repositories.TaskTemplateRepository;
import com.max.exigo.CustomerDao;
import com.max.messaging.message.MaxMessage;
import com.max.messaging.subscribe.DurableTopicSubscriber;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * This subscriber deals with keeping Associates' tasks in sync with activity performed by the subscriber.
 * Basically this means that for any Task-related event, a pending Task is searched for which relates to the
 * event performed, or created if it does not already exist, and marked complete
 */
public class AutoTaskFlowSubscriber extends DurableTopicSubscriber
{
    Logger log = Logger.getLogger(AutoTaskFlowSubscriber.class);

    @Autowired
    AutoTaskFlowRepository autoTaskFlowRepository;
    @Autowired
    AssociateTaskRepository associateTaskRepository;
    @Autowired
    TaskTemplateRepository taskTemplateRepository;
    @Autowired
    CustomerDao customerDao;
    @Autowired
    EventTaskMapping eventTaskMapping;

    @Override
    public void onMessage(MaxMessage message)
    {
        System.out.println("Starting AutoTaskFlowSubscriber onMessage " + this);
        if (message.getActor() == null || StringUtils.isBlank(message.getVerb()))
        {
            log.warn("Received a message with either no verb or no actor " + message.toString() + ".  Cannot execute AutoTaskFlow without that stuff!");
            return;
        }

        String taskDescriptionKey = eventTaskMapping.getTaskName(message.getVerb());
        List<AutoTaskFlow> taskFlows = autoTaskFlowRepository.findByTriggerTaskDescriptionKey(taskDescriptionKey);

        for (AutoTaskFlow curFlow : taskFlows)
        {
            try
            {
                Integer assigneeId = getRelatedAssociateId(message.getActor().getId(), curFlow.getAssigneeType());

                if (assigneeId != null)
                {
                    if (taskShouldBeSpun(curFlow, assigneeId, message.getSubject() != null ? message.getSubject().getId() : null))
                    {
                        AssociateTask associateTask = createNewTask(curFlow, message, assigneeId);
                        getAssociateTaskRepository().save(associateTask);
                    }
                }
                else
                {
                    log.warn("Could not find Associate of type " + curFlow.getAssigneeType() + " relative to Associate ID: " + message.getActor().getId());
                }
            }
            catch (NumberFormatException e)
            {
                log.error("An error occurred trying to get the related associate for the following message: " + message + " for AutoTaskFlow " + curFlow);
            }
        }
    }

    /**
     * Create the task per the configuration of the specified {@code AutoTaskFlow}
     *
     * @param curFlow {@code AutoTaskFlow} in compliance of which this task is being created
     * @param message {@code MaxMessage} which would be the message that triggered the {@code AutoTaskFlow}
     * @param assigneeId {@code Integer} ID of the associate to which to assign this task
     * @return {@code AssociateTask}
     */
    AssociateTask  createNewTask(AutoTaskFlow curFlow, MaxMessage message, Integer assigneeId)
    {
        AssociateTask task = new AssociateTask();
        task.setTask(curFlow.getTaskToSpin());
        task.setAssociateId(assigneeId);
        task.setCreatedDate(new Date());

        Integer dueInNumDays = curFlow.getAutoDueDateNumDays();
        if (dueInNumDays != null && dueInNumDays > 0)
        {
            task.setDueDate(DateUtils.addDays(new Date(), dueInNumDays));
        }

        MaxMessage.Subject subject = message.getSubject();
        if (subject != null)
        {
            task.setSubjectObjectType(subject.getObjectType());
            task.setSubjectId(subject.getId());
        }

        return task;
    }


    /**
     * For the type provided, retrieves the ID of the user relative to the {@code associateId}
     *
     * @param associateId  {@code Integer} of the Associate whose upline to search for the first occurrence of the specified type of user
     * @param assigneeType {@code AutoTaskFlow.ASSIGNEE_TYPE}
     * @return {@code Integer}
     */
    Integer getRelatedAssociateId(Integer associateId, AutoTaskFlow.ASSIGNEE_TYPE assigneeType)
    {
        switch (assigneeType)
        {
            case ASSOCIATE:
                return associateId;
            case ENROLLER:
                return getCustomerDao().getEnrollerId(associateId);
            case SPONSOR:
                return getCustomerDao().getSponsorId(associateId);
            case UPLINE_BRONZE:
                return getCustomerDao().getUplineBronzeId(associateId);
            case UPLINE_SILVER:
                return getCustomerDao().getUplineSilverId(associateId);
            case UPLINE_GOLD:
                return getCustomerDao().getUplineGoldId(associateId);
        }

        return null;
    }

    /**
     * <p>
     * Per the criteria of the AutoTaskFlow, determine if a Task should be spun up for the given assignee
     * </p>
     * As of its writing, this method deals with 3 criteria:
     * <ul>
     * <li>Is Repeatable - If the task flow has the {@code canRepeat} flag set to false, then never create
     * a task that's been created for a given associate and subject a second time</li>
     * <li>Depends on Another Task - If this task has a dependent task, then the task should only be spun
     * up if that task has been created and completed</li>
     * <li>Min Repeat Number of Days - If exists, prevents the task from qualifying if that number of days
     * hasn't passed since the last task was created</li>
     * </ul>
     * -
     *
     * @param curFlow    {@code AutoTaskFlow}
     * @param assigneeId {@code Integer} pre-determined recipient of the follow-up task per the {@code AutoTaskFlow}
     * @param subjectId  {@code Integer} can be null
     * @return {@code boolean}
     */
    boolean taskShouldBeSpun(@NotNull final AutoTaskFlow curFlow, @NotNull final Integer assigneeId, final Integer subjectId)
    {
        // If it's not configured to repeat, then don't create if there's ever been one for the task related to the TaskFlow
        if (!curFlow.isCanRepeat())
        {
            List<AssociateTask> preExistingAssocTasks;

            if (subjectId == null)
                preExistingAssocTasks = getAssociateTaskRepository().findByAssociateIdAndTaskDescriptionKey(assigneeId, curFlow.getTaskToSpin().getDescriptionKey());
            else
                preExistingAssocTasks = getAssociateTaskRepository().findByAssociateIdAndTaskDescriptionKeyAndSubjectId(assigneeId, curFlow.getTaskToSpin().getDescriptionKey(), subjectId);

            if (!preExistingAssocTasks.isEmpty())
                return false;
        }

        if (curFlow.getDependentTask() != null)
        {
            List<AssociateTask> preExistingAssocTasks;

            if (subjectId == null)
                preExistingAssocTasks = getAssociateTaskRepository().findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNotNull(assigneeId, curFlow.getDependentTask().getDescriptionKey());
            else
                preExistingAssocTasks = getAssociateTaskRepository().findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNotNull(assigneeId, curFlow.getDependentTask().getDescriptionKey(), subjectId);

            if (preExistingAssocTasks.isEmpty())
                return false;
        }

        if (curFlow.getMinRepeatDelayNumDays() > 0)
        {
            // TODO: Add logic to be sure not to generate a task too frequently
            log.warn("+++++++++++ You still haf'ta make sure not to create tasks too frequently!");
        }

        return true;
    }


    public AssociateTaskRepository getAssociateTaskRepository()
    {
        return associateTaskRepository;
    }

    public CustomerDao getCustomerDao()
    {
        return customerDao;
    }
}
