package com.max.messaging.subscribe.impl;

import com.max.BaseMockUnitTest;
import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.model.AutoTaskFlow;
import com.max.coaching.db.model.TaskTemplate;
import com.max.coaching.db.repositories.AssociateTaskRepository;
import com.max.exigo.CustomerDao;
import com.max.messaging.message.MaxMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Validate AutoTaskFlowSubscriber behavior
 */
public class AutoTaskFlowSubscriberMockTest extends BaseMockUnitTest
{
    /**
     * Verify the proper flow is taken when we request an Associate relative to another
     * Find Associate relative to an Associate (self)
     *
     * @throws Exception
     */
    @Test
    public void testGetRelatedAssociate() throws Exception
    {
        AutoTaskFlowSubscriber atfs = mock(AutoTaskFlowSubscriber.class);
        CustomerDao customerDao = mock(CustomerDao.class);
        when(atfs.getCustomerDao()).thenReturn(customerDao);
        when(atfs.getRelatedAssociateId(anyInt(), any(AutoTaskFlow.ASSIGNEE_TYPE.class))).thenCallRealMethod();

        Integer relatedAssociateId = atfs.getRelatedAssociateId(123, AutoTaskFlow.ASSIGNEE_TYPE.ASSOCIATE);

        assertThat(relatedAssociateId).isEqualTo(123);
        verify(customerDao, never()).getEnrollerId(anyInt());
        verify(customerDao, never()).getSponsorId(anyInt());
        verify(customerDao, never()).getUplineBronzeId(anyInt());
        verify(customerDao, never()).getUplineSilverId(anyInt());
        verify(customerDao, never()).getUplineGoldId(anyInt());
    }

    /**
     * Verify the proper flow is taken when we request an Associate relative to another
     * Find Enroller relative to an Associate
     *
     * @throws Exception
     */
    @Test
    public void testGetRelatedEnroller() throws Exception
    {
        AutoTaskFlowSubscriber atfs = mock(AutoTaskFlowSubscriber.class);
        CustomerDao customerDao = mock(CustomerDao.class);
        when(atfs.getCustomerDao()).thenReturn(customerDao);
        when(atfs.getRelatedAssociateId(anyInt(), any(AutoTaskFlow.ASSIGNEE_TYPE.class))).thenCallRealMethod();

        atfs.getRelatedAssociateId(123, AutoTaskFlow.ASSIGNEE_TYPE.ENROLLER);

        verify(customerDao).getEnrollerId(anyInt());
        verify(customerDao, never()).getSponsorId(anyInt());
        verify(customerDao, never()).getUplineBronzeId(anyInt());
        verify(customerDao, never()).getUplineSilverId(anyInt());
        verify(customerDao, never()).getUplineGoldId(anyInt());
    }

    /**
     * Verify the proper flow is taken when we request an Associate relative to another
     * Find Sponsor relative to an Associate
     *
     * @throws Exception
     */
    @Test
    public void testGetRelatedSponsor() throws Exception
    {
        AutoTaskFlowSubscriber atfs = mock(AutoTaskFlowSubscriber.class);
        CustomerDao customerDao = mock(CustomerDao.class);
        when(atfs.getCustomerDao()).thenReturn(customerDao);
        when(atfs.getRelatedAssociateId(anyInt(), any(AutoTaskFlow.ASSIGNEE_TYPE.class))).thenCallRealMethod();

        atfs.getRelatedAssociateId(123, AutoTaskFlow.ASSIGNEE_TYPE.SPONSOR);

        verify(customerDao, never()).getEnrollerId(anyInt());
        verify(customerDao).getSponsorId(anyInt());
        verify(customerDao, never()).getUplineBronzeId(anyInt());
        verify(customerDao, never()).getUplineSilverId(anyInt());
        verify(customerDao, never()).getUplineGoldId(anyInt());
    }

    /**
     * Verify the proper flow is taken when we request an Associate relative to another
     * Find Upline Bronze relative to an Associate
     *
     * @throws Exception
     */
    @Test
    public void testGetRelatedUplineBronze() throws Exception
    {
        AutoTaskFlowSubscriber atfs = mock(AutoTaskFlowSubscriber.class);
        CustomerDao customerDao = mock(CustomerDao.class);
        when(atfs.getCustomerDao()).thenReturn(customerDao);
        when(atfs.getRelatedAssociateId(anyInt(), any(AutoTaskFlow.ASSIGNEE_TYPE.class))).thenCallRealMethod();

        atfs.getRelatedAssociateId(123, AutoTaskFlow.ASSIGNEE_TYPE.UPLINE_BRONZE);

        verify(customerDao, never()).getEnrollerId(anyInt());
        verify(customerDao, never()).getSponsorId(anyInt());
        verify(customerDao).getUplineBronzeId(anyInt());
        verify(customerDao, never()).getUplineSilverId(anyInt());
        verify(customerDao, never()).getUplineGoldId(anyInt());
    }

    /**
     * Verify the proper flow is taken when we request an Associate relative to another
     * Find Upline Silver relative to an Associate
     *
     * @throws Exception
     */
    @Test
    public void testGetRelatedUplineSilver() throws Exception
    {
        AutoTaskFlowSubscriber atfs = mock(AutoTaskFlowSubscriber.class);
        CustomerDao customerDao = mock(CustomerDao.class);
        when(atfs.getCustomerDao()).thenReturn(customerDao);
        when(atfs.getRelatedAssociateId(anyInt(), any(AutoTaskFlow.ASSIGNEE_TYPE.class))).thenCallRealMethod();

        atfs.getRelatedAssociateId(123, AutoTaskFlow.ASSIGNEE_TYPE.UPLINE_SILVER);

        verify(customerDao, never()).getEnrollerId(anyInt());
        verify(customerDao, never()).getSponsorId(anyInt());
        verify(customerDao, never()).getUplineBronzeId(anyInt());
        verify(customerDao).getUplineSilverId(anyInt());
        verify(customerDao, never()).getUplineGoldId(anyInt());
    }

    /**
     * Verify the proper flow is taken when we request an Associate relative to another
     * Find Upline Gold relative to an Associate
     *
     * @throws Exception
     */
    @Test
    public void testGetRelatedUplineGold() throws Exception
    {
        AutoTaskFlowSubscriber atfs = mock(AutoTaskFlowSubscriber.class);
        CustomerDao customerDao = mock(CustomerDao.class);
        when(atfs.getCustomerDao()).thenReturn(customerDao);
        when(atfs.getRelatedAssociateId(anyInt(), any(AutoTaskFlow.ASSIGNEE_TYPE.class))).thenCallRealMethod();

        atfs.getRelatedAssociateId(123, AutoTaskFlow.ASSIGNEE_TYPE.UPLINE_GOLD);

        verify(customerDao, never()).getEnrollerId(anyInt());
        verify(customerDao, never()).getSponsorId(anyInt());
        verify(customerDao, never()).getUplineBronzeId(anyInt());
        verify(customerDao, never()).getUplineSilverId(anyInt());
        verify(customerDao).getUplineGoldId(anyInt());
    }

    /**
     * <p>
     * Verify that if an Auto Task Flow specifies an {@code automaticDueDateNumDays}, the resulting {@code AssociateTask}
     * has a proper due date
     * </p>
     * <p>
     * This test also validates that if a Message has a subject, that subject info is propagated to the resulting {@code AssociateTask}
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testCreateNewTask_WSubjectAndDueDate() throws Exception
    {
        AutoTaskFlow flow = new AutoTaskFlow();
        TaskTemplate taskToSpin = new TaskTemplate();
        taskToSpin.setDescriptionKey("TaskToSpin");
        taskToSpin.setId(123);
        flow.setTaskToSpin(taskToSpin);
        flow.setAssigneeType(AutoTaskFlow.ASSIGNEE_TYPE.UPLINE_BRONZE);
        flow.setCanRepeat(false);
        flow.setAutoDueDateNumDays(3);
        TaskTemplate triggerTask = new TaskTemplate();
        flow.setTriggerTask(triggerTask);

        AutoTaskFlowSubscriber subscriber = mock(AutoTaskFlowSubscriber.class);
        when(subscriber.createNewTask(any(AutoTaskFlow.class), any(MaxMessage.class), anyInt())).thenCallRealMethod();

        MaxMessage message = new MaxMessage();
        MaxMessage.Subject subject = new MaxMessage.Subject();
        subject.setObjectType("SomeSubjectObjectType");
        subject.setId(555);
        message.setSubject(subject);

        AssociateTask newTask = subscriber.createNewTask(flow, message, 123);

        assertThat(newTask.getSubjectObjectType()).isEqualToIgnoringCase(subject.getObjectType());
        assertThat(newTask.getSubjectId()).isEqualTo(subject.getId());
        assertThat(newTask.getTask().getId()).isEqualTo(taskToSpin.getId());
        assertThat(newTask.getDueDate()).isInSameDayAs(DateUtils.addDays(new Date(), 3));

        System.out.println("Asserted that subject and due date are populated for a flow that has auto due date settings and the message refers to a subject");
    }


    /**
     * Validate that when no {@code automaticDueDateNumDays} is specified for an Auto Task Flow,
     * that the resulting {@code AssociateTask} has no due date
     *
     * @throws Exception
     */
    @Test
    public void testCreateNewTask_WSubjectNoDueDate() throws Exception
    {
        AutoTaskFlow flow = new AutoTaskFlow();
        TaskTemplate taskToSpin = new TaskTemplate();
        taskToSpin.setDescriptionKey("TaskToSpin");
        taskToSpin.setId(123);
        flow.setTaskToSpin(taskToSpin);
        flow.setAssigneeType(AutoTaskFlow.ASSIGNEE_TYPE.UPLINE_BRONZE);
        flow.setCanRepeat(false);
        TaskTemplate triggerTask = new TaskTemplate();
        flow.setTriggerTask(triggerTask);

        AutoTaskFlowSubscriber subscriber = mock(AutoTaskFlowSubscriber.class);
        when(subscriber.createNewTask(any(AutoTaskFlow.class), any(MaxMessage.class), anyInt())).thenCallRealMethod();

        MaxMessage message = new MaxMessage();
        MaxMessage.Subject subject = new MaxMessage.Subject();
        subject.setObjectType("SomeSubjectObjectType");
        subject.setId(555);
        message.setSubject(subject);

        AssociateTask newTask = subscriber.createNewTask(flow, message, 123);

        assertThat(newTask.getSubjectObjectType()).isEqualToIgnoringCase(subject.getObjectType());
        assertThat(newTask.getSubjectId()).isEqualTo(subject.getId());
        assertThat(newTask.getTask().getId()).isEqualTo(taskToSpin.getId());
        assertThat(newTask.getDueDate()).isNull();

        System.out.println("Asserted that the lack of due date settings in a flow results in no due date set in resulting task");
    }

    /**
     * Validate that when an AutoTaskFlow doesn't allow for repeat tasks and no subject is specified, the Repository
     * that doesn't specify a SubjectId method is called to find existing tasks
     *
     * @throws Exception
     */
    @Test
    public void testTaskShouldBeSpun_NoRepeatWOSubject() throws Exception
    {
        AssociateTaskRepository repository = mock(AssociateTaskRepository.class);
        when(repository.findByAssociateIdAndTaskDescriptionKey(anyInt(), anyString())).thenReturn(Arrays.asList(new AssociateTask()));
        AutoTaskFlowSubscriber subscriber = mock(AutoTaskFlowSubscriber.class);
        when(subscriber.taskShouldBeSpun(any(AutoTaskFlow.class), anyInt(), anyInt())).thenCallRealMethod();
        when(subscriber.getAssociateTaskRepository()).thenReturn(repository);

        AutoTaskFlow flow = new AutoTaskFlow();
        flow.setCanRepeat(false);
        flow.setTaskToSpin(new TaskTemplate());

        boolean b = subscriber.taskShouldBeSpun(flow, 123, null);

        assertThat(b).isFalse();
        verify(repository).findByAssociateIdAndTaskDescriptionKey(anyInt(), anyString());
        verify(repository, never()).findByAssociateIdAndTaskDescriptionKeyAndSubjectId(anyInt(), anyString(), anyInt());
    }

    /**
     * Validate that when an {@code AutoTaskFlow} doesn't allow for repeats and an Associate has already had
     * a task of the type to be generated by the {@code AutoTaskFlow}, that AutoTaskFlowSubscriber.taskShouldBeSpun
     * returns false
     *
     * @throws Exception
     */
    @Test
    public void testTaskShouldBeSpun_NoRepeatWSubject() throws Exception
    {
        AssociateTaskRepository repository = mock(AssociateTaskRepository.class);
        when(repository.findByAssociateIdAndTaskDescriptionKeyAndSubjectId(anyInt(), anyString(), anyInt())).thenReturn(Arrays.asList(new AssociateTask()));
        AutoTaskFlowSubscriber subscriber = mock(AutoTaskFlowSubscriber.class);
        when(subscriber.taskShouldBeSpun(any(AutoTaskFlow.class), anyInt(), anyInt())).thenCallRealMethod();
        when(subscriber.getAssociateTaskRepository()).thenReturn(repository);

        AutoTaskFlow flow = new AutoTaskFlow();
        flow.setCanRepeat(false);
        flow.setTaskToSpin(new TaskTemplate());

        boolean b = subscriber.taskShouldBeSpun(flow, 123, 555);

        assertThat(b).isFalse();
        verify(repository, never()).findByAssociateIdAndTaskDescriptionKey(anyInt(), anyString());
        verify(repository).findByAssociateIdAndTaskDescriptionKeyAndSubjectId(anyInt(), anyString(), anyInt());
    }

    /**
     * Validate that when an {@code AutoTaskFlow} doesn't allow for repeats and an Associate has NOT already had
     * a task of the type to be generated by the {@code AutoTaskFlow}, that AutoTaskFlowSubscriber.taskShouldBeSpun
     * returns true
     * Also validates that the proper repository method is called to take SubjectId into account
     *
     * @throws Exception
     */
    @Test
    public void testTaskShouldBeSpun_NoRepeatWSubject_WhenNoExisting() throws Exception
    {
        AssociateTaskRepository repository = mock(AssociateTaskRepository.class);
        when(repository.findByAssociateIdAndTaskDescriptionKeyAndSubjectId(anyInt(), anyString(), anyInt())).thenReturn(Collections.<AssociateTask>emptyList());
        AutoTaskFlowSubscriber subscriber = mock(AutoTaskFlowSubscriber.class);
        when(subscriber.taskShouldBeSpun(any(AutoTaskFlow.class), anyInt(), anyInt())).thenCallRealMethod();
        when(subscriber.getAssociateTaskRepository()).thenReturn(repository);

        AutoTaskFlow flow = new AutoTaskFlow();
        flow.setCanRepeat(false);
        flow.setTaskToSpin(new TaskTemplate());

        boolean b = subscriber.taskShouldBeSpun(flow, 123, 555);

        assertThat(b).isTrue();
        verify(repository, never()).findByAssociateIdAndTaskDescriptionKey(anyInt(), anyString());
        verify(repository).findByAssociateIdAndTaskDescriptionKeyAndSubjectId(anyInt(), anyString(), anyInt());
    }

    /**
     * Validate that when an {@code AutoTaskFlow} has a dependent task, and that task is not found among those
     * completed by the assignee {@code taskShouldBeSpun}, returns false
     * Also verifies that the proper repository methods are called when checking for validity for an
     * {@code AutoTaskFlow} with a dependent task
     *
     * @throws Exception
     */
    @Test
    public void testTaskShouldBeSpun_DependentTask_NotFound() throws Exception
    {
        AssociateTaskRepository repository = mock(AssociateTaskRepository.class);
        when(repository.findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNotNull(anyInt(), anyString())).thenReturn(Collections.<AssociateTask>emptyList());
        AutoTaskFlowSubscriber subscriber = mock(AutoTaskFlowSubscriber.class);
        when(subscriber.taskShouldBeSpun(any(AutoTaskFlow.class), anyInt(), anyInt())).thenCallRealMethod();
        when(subscriber.getAssociateTaskRepository()).thenReturn(repository);

        AutoTaskFlow flow = new AutoTaskFlow();
        flow.setDependentTask(new TaskTemplate());
        flow.setTaskToSpin(new TaskTemplate());

        boolean b = subscriber.taskShouldBeSpun(flow, 123, null);

        assertThat(b).isFalse();
        verify(repository).findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNotNull(anyInt(), anyString());
        verify(repository, never()).findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNotNull(anyInt(), anyString(), anyInt());
    }

    /**
     * Validate that when an {@code AutoTaskFlow} has a dependent task, and that task is found among those
     * completed by the assignee {@code taskShouldBeSpun}, returns true
     * Also verifies that the proper repository methods are called when checking for validity for an
     * {@code AutoTaskFlow} with a dependent task
     *
     * @throws Exception
     */
    @Test
    public void testTaskShouldBeSpun_DependentTask_Found() throws Exception
    {
        AssociateTaskRepository repository = mock(AssociateTaskRepository.class);
        when(repository.findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNotNull(anyInt(), anyString())).thenReturn(Collections.singletonList(new AssociateTask()));
        AutoTaskFlowSubscriber subscriber = mock(AutoTaskFlowSubscriber.class);
        when(subscriber.taskShouldBeSpun(any(AutoTaskFlow.class), anyInt(), anyInt())).thenCallRealMethod();
        when(subscriber.getAssociateTaskRepository()).thenReturn(repository);

        AutoTaskFlow flow = new AutoTaskFlow();
        flow.setDependentTask(new TaskTemplate());
        flow.setTaskToSpin(new TaskTemplate());

        boolean b = subscriber.taskShouldBeSpun(flow, 123, null);

        assertThat(b).isTrue();
        verify(repository).findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNotNull(anyInt(), anyString());
        verify(repository, never()).findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNotNull(anyInt(), anyString(), anyInt());
    }

    /**
     * Validate that when an {@code AutoTaskFlow} has a dependent task, and that task is not found among those
     * completed by the assignee {@code taskShouldBeSpun}, returns false
     * Also verifies that the proper repository methods are called when checking for validity for an
     * {@code AutoTaskFlow} with a dependent task and a subject Id is specified
     *
     * @throws Exception
     */
    @Test
    public void testTaskShouldBeSpun_DependentTask_Found_WSubject() throws Exception
    {
        AssociateTaskRepository repository = mock(AssociateTaskRepository.class);
        when(repository.findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNotNull(anyInt(), anyString(), anyInt())).thenReturn(Collections.singletonList(new AssociateTask()));
        AutoTaskFlowSubscriber subscriber = mock(AutoTaskFlowSubscriber.class);
        when(subscriber.taskShouldBeSpun(any(AutoTaskFlow.class), anyInt(), anyInt())).thenCallRealMethod();
        when(subscriber.getAssociateTaskRepository()).thenReturn(repository);

        AutoTaskFlow flow = new AutoTaskFlow();
        flow.setDependentTask(new TaskTemplate());
        flow.setTaskToSpin(new TaskTemplate());

        subscriber.taskShouldBeSpun(flow, 123, 555);

        verify(repository, never()).findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNotNull(anyInt(), anyString());
        verify(repository).findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNotNull(anyInt(), anyString(), anyInt());
    }
}
