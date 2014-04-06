package com.max.messaging.subscribe.impl;

import com.max.BaseMockUnitTest;
import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.model.TaskTemplate;
import com.max.coaching.db.repositories.AssociateTaskRepository;
import com.max.coaching.db.repositories.TaskTemplateRepository;
import com.max.messaging.message.MaxMessage;
import org.junit.Test;

import java.util.*;

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

    /**
     * Verify that if we have a subject in our message, that we add the subject as criteria for looking for existing tasks
     *
     * @throws Exception
     */
    @Test
    public void testGetAssociateTaskWSubject() throws Exception
    {
        TaskSubscriber taskSubscriber = mock(TaskSubscriber.class);
        when(taskSubscriber.getEventTaskMapping()).thenReturn(createEventTaskMap());
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

        System.out.println("Asserted existing Task are searched including subject data when Message includes subject data");
    }

    /**
     * Verify that if we don't have a subject in our message, that we don't add the subject as criteria for looking for existing tasks
     *
     * @throws Exception
     */
    @Test
    public void testGetAssociateTaskWOutSubject() throws Exception
    {
        TaskSubscriber taskSubscriber = mock(TaskSubscriber.class);
        when(taskSubscriber.getEventTaskMapping()).thenReturn(createEventTaskMap());
        when(taskSubscriber.getAssociateTask(any(MaxMessage.class), anyString())).thenCallRealMethod();

        AssociateTaskRepository taskRepository = mock(AssociateTaskRepository.class);
        when(taskSubscriber.getAssociateTaskRepository()).thenReturn(taskRepository);

        MaxMessage message = new MaxMessage();
        MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setObjectType("Actor.ObjectType");
        actor.setId(123);
        message.setActor(actor);

        message.setVerb("LoggedIn");

        //
        // Action!
        taskSubscriber.getAssociateTask(message, "LOG_IN");

        // verify
        verify(taskRepository, never()).findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNull(anyInt(), anyString(), anyInt());
        verify(taskRepository).findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNull(anyInt(), anyString());

        System.out.println("Asserted existing Associate tasks are searched without subject data when Message has no subject");
    }

    /**
     * Verify that if we find no existing tasks, that we create a new one
     *
     * @throws Exception
     */
    @Test
    public void testGetAssociateTaskWOutSubject_NoExistingTaskFound() throws Exception
    {
        TaskSubscriber taskSubscriber = mock(TaskSubscriber.class);
        when(taskSubscriber.getEventTaskMapping()).thenReturn(createEventTaskMap());
        when(taskSubscriber.getAssociateTask(any(MaxMessage.class), anyString())).thenCallRealMethod();

        AssociateTaskRepository taskRepository = mock(AssociateTaskRepository.class);
        when(taskSubscriber.getAssociateTaskRepository()).thenReturn(taskRepository);
        when(taskRepository.findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNull(anyInt(), anyString())).thenReturn(Collections.<AssociateTask>emptyList());

        MaxMessage message = new MaxMessage();
        MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setObjectType("Actor.ObjectType");
        actor.setId(123);
        message.setActor(actor);

        message.setVerb("LoggedIn");

        //
        // Action!
        taskSubscriber.getAssociateTask(message, "LOG_IN");

        // verify
        verify(taskRepository, never()).findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNull(anyInt(), anyString(), anyInt());
        verify(taskRepository).findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNull(anyInt(), anyString());
        verify(taskSubscriber).createNewActivityRecord(any(MaxMessage.class), anyString());

        System.out.println("Asserted that if no existing pending task is found for an associate and event, a new one will be created");
    }

    /**
     * Verify that if we find existing tasks, that we don't create a new one
     *
     * @throws Exception
     */
    @Test
    public void testGetAssociateTaskWOutSubject_ExistingTaskFound() throws Exception
    {
        TaskSubscriber taskSubscriber = mock(TaskSubscriber.class);
        when(taskSubscriber.getEventTaskMapping()).thenReturn(createEventTaskMap());
        when(taskSubscriber.getAssociateTask(any(MaxMessage.class), anyString())).thenCallRealMethod();

        AssociateTaskRepository taskRepository = mock(AssociateTaskRepository.class);
        when(taskSubscriber.getAssociateTaskRepository()).thenReturn(taskRepository);
        List<AssociateTask> existingTasks = new LinkedList<>();
        existingTasks.add(new AssociateTask());
        when(taskRepository.findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNull(anyInt(), anyString())).thenReturn(existingTasks);

        MaxMessage message = new MaxMessage();
        MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setObjectType("Actor.ObjectType");
        actor.setId(123);
        message.setActor(actor);

        message.setVerb("LoggedIn");

        //
        // Action!
        taskSubscriber.getAssociateTask(message, "LOG_IN");

        // verify
        verify(taskRepository, never()).findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNull(anyInt(), anyString(), anyInt());
        verify(taskRepository).findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNull(anyInt(), anyString());
        verify(taskSubscriber, never()).createNewActivityRecord(any(MaxMessage.class), anyString());

        System.out.println("Asserted that the existing Associate Task is used if one is found pending for the associate/event in a message");
    }

    /**
     * Verfify the functionality to populate completed data
     */
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
        assertThat(associateTask.getSubjectObjectType()).isEqualTo(subject.getObjectType());

        System.out.println("Asserted all expected fields are set when populating completed data in an Associate Task");
    }

    /**
     * Verfify the functionality to create a new AssociateTask
     */
    @Test
    public void testCreateNewActivityRecord()
    {
        MaxMessage message = new MaxMessage();
        MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setId(123);
        actor.setObjectType("associate");
        message.setActor(actor);
        MaxMessage.Subject subject = new MaxMessage.Subject();
        subject.setId(555);
        subject.setObjectType("subjectObject");
        message.setSubject(subject);

        TaskSubscriber taskSubscriber = mock(TaskSubscriber.class);
        when(taskSubscriber.createNewActivityRecord(any(MaxMessage.class), anyString())).thenCallRealMethod();

        TaskTemplate task = new TaskTemplate();
        task.setId(333);

        TaskTemplateRepository taskTemplateRepository = mock(TaskTemplateRepository.class);
        when(taskTemplateRepository.findByDescriptionKey(anyString())).thenReturn(task);
        when(taskSubscriber.getTaskTemplateRepository()).thenReturn(taskTemplateRepository);

        AssociateTask myTask = taskSubscriber.createNewActivityRecord(message, "MyTask");
        assertThat(myTask.getCompletedDate()).isNull();
        assertThat(myTask.getCreatedDate()).isInSameDayAs(new Date());
        assertThat(myTask.getTask()).isNotNull();
        assertThat(myTask.getTask().getId()).isEqualTo(333);
        assertThat(myTask.getSubjectId()).isEqualTo(subject.getId());
        assertThat(myTask.getSubjectObjectType()).isEqualTo(subject.getObjectType());

        System.out.println("Asserted that all expected fields are populated when creating an Associate Task from a message");
    }

    private EventTaskMapping createEventTaskMap()
    {
        HashMap<String, String> eventTaskMap = new HashMap<>();
        eventTaskMap.put("LoggedIn", "LOG_IN");
        eventTaskMap.put("ProspectAdded", "ADD_PROSPECT");
        eventTaskMap.put("ProspectContacted", "CONTACT_PROSPECT");
        eventTaskMap.put("VideoWatched", "WATCH_VIDEO");
        eventTaskMap.put("WhyAdded", "ADD_WHY");
        eventTaskMap.put("WhyPhotoUploaded", "UPLOAD_WHY_PHOTO");
        eventTaskMap.put("AutoShipAdded", "CREATE_AUTOSHIP");

        EventTaskMapping mapping = new EventTaskMapping();
        mapping.setEventTaskMap(eventTaskMap);
        return mapping;
    }
}
