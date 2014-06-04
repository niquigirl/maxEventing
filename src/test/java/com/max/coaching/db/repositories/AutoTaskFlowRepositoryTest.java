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
 * Validate AutoTaskFlowRepository functions
 */
public class AutoTaskFlowRepositoryTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    AutoTaskFlowRepository atfRepository;

    @Autowired
    TaskTemplateRepository ttRepository;

    @Before
    public void setup()
    {
        TaskTemplate task = new TaskTemplate();
        task.setUrl("www.url.com");
        task.setFormUrl("www.form.com");
        task.setDetailUrl("www.detail.com");
        task.setDescriptionKey("DESC_KEY");
        task.setTaskClass("SOME_CLASS");

        ttRepository.saveAndFlush(task);

        AutoTaskFlow newFlow = new AutoTaskFlow();
        newFlow.setAssigneeType(AutoTaskFlow.ASSIGNEE_TYPE.ASSOCIATE);
        newFlow.setAutoDueDateNumDays(5);
        newFlow.setCanRepeat(true);
        newFlow.setTriggerTask(task);
        newFlow.setDependentTask(task);
        newFlow.setTaskToSpin(task);

        System.out.println(atfRepository.save(newFlow));
    }

    @Test
    public void testFindByEventName() throws Exception
    {
        List<AutoTaskFlow> events = atfRepository.findByTriggerTaskDescriptionKey("DESC_KEY");

        assertThat(events).isNotEmpty();

        System.out.println("Asserted we can retrieve AutTaskFlow by trigger task: " + events.get(0).getTriggerTask().getDescriptionKey());
    }
}
