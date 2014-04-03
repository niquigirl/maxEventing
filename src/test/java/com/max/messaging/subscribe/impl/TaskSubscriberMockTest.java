package com.max.messaging.subscribe.impl;

import com.max.BaseMockUnitTest;
import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.model.TaskTemplate;
import com.max.coaching.db.repositories.AssociateTaskRepository;
import com.max.coaching.db.repositories.TaskTemplateRepository;
import com.max.messaging.message.MaxMessage;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Validate logic behind TaskSubscriber
 */
public class TaskSubscriberMockTest extends BaseMockUnitTest
{

    @Test
    public void testGetAssociateTask() throws Exception
    {
        TaskSubscriber taskSubscriber = mock(TaskSubscriber.class);
        HashMap<String, String> eventTaskMap = createEventTaskMap();
        when(taskSubscriber.getEventTaskMap()).thenReturn(eventTaskMap);
        when(taskSubscriber.getAssociateTask(any(MaxMessage.class), anyString())).thenCallRealMethod();

        AssociateTaskRepository taskRepository = mock(AssociateTaskRepository.class);
        when(taskSubscriber.getAssociateTaskRepository()).thenReturn(taskRepository);

        MaxMessage message = new MaxMessage();
        MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setObjectType("Actor.ObjectType");
        actor.setId(123);
        message.setActor(actor);

        MaxMessage.Subject subject = new MaxMessage.Subject();
        subject.setObjectType("Subject.ObjectType");
        subject.setId(444);
        message.setSubject(subject);

        message.setVerb("LoggedIn");

        //
        // Action!
        taskSubscriber.getAssociateTask(message, "LOG_IN");

        // verify
        verify(taskRepository).findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNull(anyInt(), anyString(), anyInt());
        verify(taskRepository, never()).findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNull(anyInt(), anyString());
    }

    @Test
    public void testPopulateData()
    {
        TaskSubscriber taskSubscriber = mock(TaskSubscriber.class);
        doCallRealMethod().when(taskSubscriber).populateCompletedData(any(MaxMessage.class), any(AssociateTask.class));

        MaxMessage maxMessage = new MaxMessage();
        MaxMessage.Subject subject = new MaxMessage.Subject();
        subject.setId(1234);
        maxMessage.setSubject(subject);

        AssociateTask associateTask = new AssociateTask();

        // Action!
        taskSubscriber.populateCompletedData(maxMessage, associateTask);

        assertThat(associateTask.getCompletedDate()).isInSameDayAs(new Date());
        assertThat(associateTask.getSubjectId()).isNotNull();
        assertThat(associateTask.getSubjectId()).isEqualTo(subject.getId());
        assertThat(associateTask.getSubjectObjectType()).isNullOrEmpty();
    }

    @Test
    public void testCreateNewActivityRecord()
    {
        TaskSubscriber taskSubscriber = mock(TaskSubscriber.class);
        when(taskSubscriber.createNewActivityRecord(any(MaxMessage.class), anyString())).thenCallRealMethod();

        TaskTemplateRepository taskTemplateRepository = mock(TaskTemplateRepository.class);
        when(taskSubscriber.getTaskTemplateRepository()).thenReturn(taskTemplateRepository);
        when(taskTemplateRepository.findByDescriptionKey(anyString())).thenReturn(mock(TaskTemplate.class));

//        verify()
    /*
        AssociateTask createNewActivityRecord(MaxMessage message, String taskName)
    {
        AssociateTask task = new AssociateTask();
        TaskTemplate byDescriptionKey = taskTemplateRepository.findByDescriptionKey(taskName);

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

     */


/*
        activityRecord.setSubjectId(message.getSubject() != null ? message.getSubject().getId() : null);
        activityRecord.setCompletedDate(new Date());
        activityRecord.setSubjectObjectType(message.getSubject() != null ? message.getSubject().getObjectType() : null);




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

 */



        /*
        doCallRealMethod().when(taskSubscriber).onMessage(any(MaxMessage.class));
        AssociateTask associateTask = mock(AssociateTask.class);
        when(taskSubscriber.getAssociateTask(any(MaxMessage.class), anyString())).thenReturn(associateTask);

        MaxMessage message = new MaxMessage();
        MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setObjectType("Actor.ObjectType");
        actor.setId(123);
        message.setActor(actor);

        MaxMessage.Subject subject = new MaxMessage.Subject();
        subject.setObjectType("Subject.ObjectType");
        subject.setId(444);
        message.setSubject(subject);

        message.setVerb("LoggedIn");

        // Scene 1
        taskSubscriber.onMessage(message);

        verify(taskRepository).findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNull(anyInt(), anyString(), anyInt());

        /*
        if (message.getSubject() != null && message.getSubject().getId() != null)
        {
            incompleteExistingTasks =
                    associateTaskRepository.findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNull(message.getActor().getId(), taskDescriptionKey, message.getSubject().getId());
        }
        else
        {
            incompleteExistingTasks =
                    associateTaskRepository.findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNull(message.getActor().getId(), taskDescriptionKey);
        }

        AssociateTask activityRecord;
        if (incompleteExistingTasks != null && !incompleteExistingTasks.isEmpty())
            activityRecord = incompleteExistingTasks.get(0);
        else
            activityRecord = createNewActivityRecord(message, taskDescriptionKey);

        populateCompletedData(message, activityRecord);

        associateTaskRepository.save(activityRecord);

         */
    }

    private HashMap<String, String> createEventTaskMap()
    {
        HashMap<String, String> eventTaskMap = new HashMap<String, String>();
        eventTaskMap.put("LoggedIn", "LOG_IN");
        eventTaskMap.put("ProspectAdded", "ADD_PROSPECT");
        eventTaskMap.put("ProspectContacted", "CONTACT_PROSPECT");
        eventTaskMap.put("VideoWatched", "WATCH_VIDEO");
        eventTaskMap.put("WhyAdded", "ADD_WHY");
        eventTaskMap.put("WhyPhotoUploaded", "UPLOAD_WHY_PHOTO");
        eventTaskMap.put("AutoShipAdded", "CREATE_AUTOSHIP");
        return eventTaskMap;
    }

    public void testTaskShouldBeSpun() throws Exception
    {
        // todo
    }
}
