package com.max.coaching.db.repositories;

import com.max.BaseSpringInjectionUnitTest;
import com.max.coaching.db.model.AutoTaskFlow;
import com.max.coaching.db.model.TaskTemplate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by neastman on 3/28/14.
 */
public class AutoTaskFlowRepositoryTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    AutoTaskFlowRepository atfRepository;

    @Autowired
    TaskTemplateRepository ttRepository;

    @Before
    public void setup() throws Exception
    {
        TaskTemplate task = new TaskTemplate();
        task.setUrl("www.url.com");
        task.setFormUrl("www.form.com");
        task.setDetailUrl("www.detail.com");
        task.setDescriptionKey("DESC_KEY");

        ttRepository.saveAndFlush(task);

        AutoTaskFlow newFlow = new AutoTaskFlow();
        newFlow.setAssigneeType(AutoTaskFlow.ASSIGNEE_TYPE.ASSOCIATE);
        newFlow.setAutoDueDateNumDays(5);
        newFlow.setCanRepeat(true);
        newFlow.setEventName("SomeEvent");
        newFlow.setDependentTask(task);
        newFlow.setTaskToSpin(task);

        atfRepository.save(newFlow);
    }

    @Test
    public void testFindByEventName() throws Exception
    {
        List<AutoTaskFlow> events = atfRepository.findByEventName("SomeEvent");

        assertThat(events).isNotEmpty();

        System.out.println("Found my AutoTaskFlow by event name " + events.size());
    }
}
