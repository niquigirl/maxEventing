package com.max.web.subscribers;

import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.model.TaskTemplate;
import com.max.coaching.db.repositories.AssociateTaskRepository;
import com.max.coaching.db.repositories.AutoTaskFlowRepository;
import com.max.coaching.db.repositories.TaskTemplateRepository;
import com.max.exigo.CustomerDao;
import com.max.web.controller.EventTaskMapping;
import com.max.web.model.MaxMessage;
import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * This subscriber deals with keeping Associates' tasks in sync with activity performed by the subscriber.
 * Basically this means that for any Task-related event, a pending Task is searched for which relates to the
 * event performed, or created if it does not already exist, and marked complete
 */
/**
 * This subscriber deals with keeping Associates' tasks in sync with activity performed by the subscriber.
 * Basically this means that for any Task-related event, a pending Task is searched for which relates to the
 * event performed, or created if it does not already exist, and marked complete
 */
@Controller
public class TaskActivityRecorderSubscriberController
{
    Logger log = Logger.getLogger(TaskActivityRecorderSubscriberController.class);

    @Autowired
    TaskTemplateRepository taskTemplateRepository;
    @Autowired
    AutoTaskFlowRepository autoTaskFlowRepository;
    @Autowired
    AssociateTaskRepository associateTaskRepository;
    @Autowired
    CustomerDao customerDao;
    @Autowired
    EventTaskMapping eventTaskMapping;

    @SuppressWarnings("unused")
    @RequestMapping(value = "{version}/{lang}/{country}/testTaskActivityRecorder", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public HandlerResults onTestMessage(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                              @NotNull @RequestBody MaxMessage message)
    {
        log.info("Running TaskActivityRecorderSubscriptionController.onTestMessage : " + message);
        HandlerResults results = new HandlerResults();
        results.setMessage(message.toString());
        results.setSuccess(true);

        return results;
    }

    @SuppressWarnings("unused")
    @RequestMapping(value = "{version}/{lang}/{country}/runTaskActivityRecorder", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public HandlerResults onMessage(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                          @NotNull @RequestBody MaxMessage message)
    {
        log.info("Running TaskActivityRecorderSubscriptionController.onMessage : " + message);

        HandlerResults results = new HandlerResults();

        String taskDescriptionKey = getEventTaskMapping().getTaskName(message.getVerb());
        if (taskDescriptionKey == null)
        {
            final String failMessage = "Could not record activity for message verb " + message.getVerb() + ". Exiting process";
            log.warn(failMessage);
            results.setSuccess(false);
            results.setMessage(failMessage);
            return results;
        }

        AssociateTask activityRecord = getAssociateTask(message, taskDescriptionKey);

        populateCompletedData(message, activityRecord);

        getAssociateTaskRepository().save(activityRecord);

        results.setMessage("TaskActivityRecorder complete");
        results.setSuccess(true);

        return results;
    }

    /**
     * Either get an existing task that's not yet completed that matches the event in the message or create a new one
     *
     * @param message            {@code MaxMessage}
     * @param taskDescriptionKey {@code String}
     * @return {@code AssociateTask} which may or may not exist in the DB
     */
    AssociateTask getAssociateTask(MaxMessage message, String taskDescriptionKey)
    {
        List<AssociateTask> incompleteExistingTasks;
        if (message.getObject() != null && message.getObject().getId() != null)
        {
            incompleteExistingTasks =
                    getAssociateTaskRepository().findByAssociateIdAndTaskTaskClassAndSubjectIdAndCompletedDateIsNull(message.getActor().getId(), taskDescriptionKey, message.getObject().getId());
        }
        else
        {
            incompleteExistingTasks =
                    getAssociateTaskRepository().findByAssociateIdAndTaskTaskClassAndCompletedDateIsNull(message.getActor().getId(), taskDescriptionKey);
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
     * @param message        {@code MaxMessage}
     * @param activityRecord {@code AssociateTask}
     */
    void populateCompletedData(MaxMessage message, AssociateTask activityRecord)
    {
        activityRecord.setCompletedDate(new Date());
        activityRecord.setSubjectId(message.getObject() != null ? message.getObject().getId() : null);
        activityRecord.setSubjectObjectType(message.getObject() != null ? message.getObject().getObjectType() : null);
    }

    /**
     * In the event no Associate Task was found in the DB to harness the action/event just received, create a new one
     *
     * @param message  {@code MaxMessage}
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
        task.setSubjectId(message.getObject() != null ? message.getObject().getId() : null);
        task.setSubjectObjectType(message.getObject() != null ? message.getObject().getObjectType() : null);

        return task;
    }


    public AssociateTaskRepository getAssociateTaskRepository()
    {
        return associateTaskRepository;
    }

    public TaskTemplateRepository getTaskTemplateRepository()
    {
        return taskTemplateRepository;
    }

    public EventTaskMapping getEventTaskMapping()
    {
        return eventTaskMapping;
    }

}

