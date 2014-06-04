package com.max.coaching.db.repositories;

import com.max.BaseSpringInjectionUnitTest;
import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.model.TaskTemplate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Validate AssociateTaskRepository stuff
 */
public class AssociateTaskRepositoryTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    TaskTemplateRepository taskTemplateRepository;

    @Autowired
    AssociateTaskRepository associateTaskRepository;

    private static TaskTemplate savedTemplate;

    @Before
    public void setup()
    {
        TaskTemplate fooTemplate = new TaskTemplate();
        fooTemplate.setDescriptionKey("SOME_DESCRIPTION");
        fooTemplate.setUrl("www.url.com");
        fooTemplate.setTaskClass("SOME_TASK_CLASS");

        savedTemplate = taskTemplateRepository.saveAndFlush(fooTemplate);

        AssociateTask assTask = new AssociateTask();
        assTask.setAssociateId(1);
        assTask.setCreatedDate(new Date());
        assTask.setTask(savedTemplate);

        associateTaskRepository.save(assTask);
    }


    @Test
    public void testFindByAssociateIdAndTaskIn() throws Exception
    {
        LinkedList<TaskTemplate> tasks = new LinkedList<>();
        tasks.add(savedTemplate);

        List<AssociateTask> tasksFoundForAssoc = associateTaskRepository.findByAssociateIdAndSubjectIdAndTaskInOrderByCreatedDateDesc(1, null, tasks);

        assertThat(tasksFoundForAssoc).isNotEmpty();
    }

    @Test
    public void testFindByAssociateAndTaskKey()
    {
        assertThat(associateTaskRepository.findByAssociateIdAndTaskDescriptionKey(1, "SOME_DESCRIPTION")).isNotEmpty();
    }
}
