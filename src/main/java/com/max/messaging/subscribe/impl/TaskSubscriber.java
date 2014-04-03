package com.max.messaging.subscribe.impl;

import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.model.AutoTaskFlow;
import com.max.coaching.db.model.TaskTemplate;
import com.max.coaching.db.repositories.AssociateTaskRepository;
import com.max.coaching.db.repositories.AutoTaskFlowRepository;
import com.max.coaching.db.repositories.TaskTemplateRepository;
import com.max.exigo.CustomerDao;
import com.max.messaging.message.MaxMessage;
import com.max.messaging.subscribe.DurableTopicSubscriber;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * This subscriber deals with keeping Associates' tasks in sync with activity performed by the subscriber.
 * Basically this means that for any Task-related event, a pending Task is searched for which relates to the
 * event performed, or created if it does not already exist, and marked complete
 */
public class TaskSubscriber extends DurableTopicSubscriber
{
    Logger log = Logger.getLogger(TaskSubscriber.class);

    @Autowired
    TaskTemplateRepository taskTemplateRepository;
    @Autowired
    AutoTaskFlowRepository autoTaskFlowRepository;
    @Autowired
    AssociateTaskRepository associateTaskRepository;
    @Autowired
    CustomerDao customerDao;

    Map<String, String> eventTaskMap;

    @Override
    public void onMessage(MaxMessage message)
    {
        String taskDescriptionKey = getEventTaskMap().get(message.getVerb());
        if (taskDescriptionKey == null)
        {
            log.error("Could not record activity for message verb " + message.getVerb() + ". Exiting process");
            return;
        }

        AssociateTask activityRecord = getAssociateTask(message, taskDescriptionKey);

        populateCompletedData(message, activityRecord);

        getAssociateTaskRepository().save(activityRecord);
    }

    /**
     * Either get an existing task that's not yet completed that matches the event in the message or create a new one
     *
     * @param message {@code MaxMessage}
     * @param taskDescriptionKey {@code String}
     * @return {@code AssociateTask} which may or may not exist in the DB
     */
    AssociateTask getAssociateTask(MaxMessage message, String taskDescriptionKey)
    {
        List<AssociateTask> incompleteExistingTasks = null;
        if (message.getSubject() != null && message.getSubject().getId() != null)
        {
            incompleteExistingTasks =
                    getAssociateTaskRepository().findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNull(message.getActor().getId(), taskDescriptionKey, message.getSubject().getId());
        }
        else
        {
            incompleteExistingTasks =
                    getAssociateTaskRepository().findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNull(message.getActor().getId(), taskDescriptionKey);
        }

        AssociateTask activityRecord;
        if (incompleteExistingTasks != null && !incompleteExistingTasks.isEmpty())
            activityRecord = incompleteExistingTasks.get(0);
        else
            activityRecord = createNewActivityRecord(message, taskDescriptionKey);
        return activityRecord;
    }

    /**
     * Populate the Associate Task with the subject and timing info for completion
     *
     * @param message {@code MaxMessage}
     * @param activityRecord {@code AssociateTask}
     */
    void populateCompletedData(MaxMessage message, AssociateTask activityRecord)
    {
        activityRecord.setSubjectId(message.getSubject() != null ? message.getSubject().getId() : null);
        activityRecord.setCompletedDate(new Date());
        activityRecord.setSubjectObjectType(message.getSubject() != null ? message.getSubject().getObjectType() : null);
    }

    /**
     * In the event no Associate Task was found in the DB to harness the action/event just received, create a new one
     *
     * @param message {@code MaxMessage}
     * @param taskName {@code String}
     * @return {@code AssociateTask}
     */
    AssociateTask createNewActivityRecord(MaxMessage message, String taskName)
    {
        AssociateTask task = new AssociateTask();
        TaskTemplate byDescriptionKey = getTaskTemplateRepository().findByDescriptionKey(taskName);

        if (byDescriptionKey == null)
        {
            log.error("Could not find a TaskTemplate for " + taskName);
            return null;
        }

        task.setTask(byDescriptionKey);
        task.setAssociateId(message.getActor().getId());
        task.setCreatedDate(new Date());
        task.setCompletedDate(new Date());
        task.setSubjectId(message.getSubject() != null ? message.getSubject().getId() : null);
        task.setSubjectObjectType(message.getSubject() != null ? message.getSubject().getObjectType(): null);

        return task;
    }

    private Integer getRelatedAssociateId(Integer associateId, AutoTaskFlow.ASSIGNEE_TYPE assigneeType)
    {
        switch (assigneeType)
        {
            case ASSOCIATE:
                return associateId;
            case ENROLLER:
                return customerDao.getEnrollerId(associateId);
            case SPONSOR:
                return customerDao.getSponsorId(associateId);
            case UPLINE_BRONZE:
                return customerDao.getUplineBronzeId(associateId);
            case UPLINE_GOLD:
                return customerDao.getUplineGoldId(associateId);
        }

        return null;
    }

    boolean taskShouldBeSpun(AutoTaskFlow curFlow, Integer assigneeId)
    {
        // TODO:  remove this
        if (true)
            return true;

        if (!curFlow.isCanRepeat())
        {
            List<AssociateTask> preExistingAssocTasks = associateTaskRepository.findByAssociateIdAndTaskDescriptionKey(assigneeId, curFlow.getTaskToSpin().getDescriptionKey());
            if (!preExistingAssocTasks.isEmpty())
                return false;
        }

        if (curFlow.getDependentTask() != null)
        {
            List<AssociateTask> preExistingAssocTasks = associateTaskRepository.findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNotNull(assigneeId, curFlow.getDependentTask().getDescriptionKey());
            if (preExistingAssocTasks.isEmpty())
                return false;
        }

        if (curFlow.getMinRepeatDelayNumDays() > 0)
        {
            // TODO: Add logic to be sure not to generate a task too frequently
        }

        return true;
    }

    /*


        // Generate related tasks
/*
        List<AutoTaskFlow> relatedAutoTasks = autoTaskFlowRepository.findByEventName(message.getVerb());

THIS SUBSCRIBER DOES NOT DEAL WITH AUTO TASK GENERATION
        for (AutoTaskFlow curFlow : relatedAutoTasks)
        {
            try
            {
                Integer assigneeId = getRelatedAssociateId(Integer.valueOf(message.getActor().getId()), curFlow.getAssigneeType());

                if (taskShouldBeSpun(curFlow, assigneeId))
                {
                    AssociateTask associateTask = spinATask(curFlow, assigneeId, message.getSubject());
                    associateTaskRepository.save(associateTask);
                }
            }
            catch (NumberFormatException e)
            {
                log.error("An error occurred trying to get the related associate for the following message: " + message + " for AutoTaskFlow " + curFlow);
            }
        }

     * A TaskFlow is going to reference some tasks, either the task it's spinning up or one that it's dependent on
     * Get all tasks for the task assignee-to-be so we can make sure they should get this task spun
     *
     * @param curFlow   {@code AutoTaskFlow} Flow for which to retrieve any tasks that might be relevant to the one to be spun
     *                  up for this flow
     * @param assignee  {@code Integer} Pre-determined assignee for the task to be spun up for this flow
     * @param subjectId {@code Integer} The id of the subject of the trigger task
     * @return {@code List&lt;AssociateTask&gt;}
     * /
    private HashMap<String, List<AssociateTask>> getRelevantAssociateTasks(AutoTaskFlow curFlow, Integer assignee, Integer subjectId)
    {
        HashMap<String, List<AssociateTask>> sortedTasks = new HashMap<>();

        List<TaskTemplate> relevantTasks = new LinkedList<>();

        if (curFlow.getDependentTask() != null)
        {
            relevantTasks.add(curFlow.getDependentTask());
        }

        if (!curFlow.isCanRepeat())
        {
            relevantTasks.add(curFlow.getTaskToSpin());
        }

        List<AssociateTask> associateTasks =
                associateTaskRepository.findByAssociateIdAndSubjectIdAndTaskInOrderByCreatedDateDesc(assignee, subjectId, relevantTasks);
        for (AssociateTask curAssocTask : associateTasks)
        {
            String taskKey = curAssocTask.getTask().getDescriptionKey();
            List<AssociateTask> taskGroup = sortedTasks.get(taskKey);
            if (taskGroup == null)
                taskGroup = new LinkedList<>();

            taskGroup.add(curAssocTask);
            sortedTasks.put(taskKey, taskGroup);
        }

        return sortedTasks;
    }
    */

    public Map<String, String> getEventTaskMap()
    {
        return eventTaskMap;
    }

    public void setEventTaskMap(Map<String, String> eventTaskMap)
    {
        this.eventTaskMap = eventTaskMap;
    }

    public AssociateTaskRepository getAssociateTaskRepository()
    {
        return associateTaskRepository;
    }

    public TaskTemplateRepository getTaskTemplateRepository()
    {
        return taskTemplateRepository;
    }
}
