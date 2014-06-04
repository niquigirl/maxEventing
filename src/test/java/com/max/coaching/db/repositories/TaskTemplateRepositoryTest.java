package com.max.coaching.db.repositories;

import com.max.BaseSpringInjectionUnitTest;
import com.max.coaching.db.model.TaskTemplate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Validate behavior of TaskTemplateRepository
 */
public class TaskTemplateRepositoryTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    TaskTemplateRepository repository;

    private static TaskTemplate savedTemplate;

    @Before
    public void setup()
    {
        TaskTemplate foo = new TaskTemplate();
        foo.setDescriptionKey("DESC-KEY");
        foo.setDetailUrl("www.detail.com");
        foo.setFormUrl("www.form.com");
        foo.setUrl("www.url.com");
        foo.setTaskClass("SOME_CLASS");

        savedTemplate = repository.save(foo);
    }

    @Test
    public void testFindOne()
    {
        TaskTemplate found = repository.findOne(savedTemplate.getId());

        assertThat(found).isNotNull();

        System.out.println("Found the test task template "+ found.getId());
    }
}
