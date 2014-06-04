package com.max.coaching.db.repositories;

import com.max.BaseSpringInjectionUnitTest;
import com.max.coaching.db.model.NotificationTemplate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Validate functionality and plumbing of NotificationTemplateRepository
 */
public class NotificationTemplateRepositoryTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    NotificationTemplateRepository notificationTemplateRepository;

    private static NotificationTemplate saved;

    @Before
    public void setup()
    {
        System.out.println(notificationTemplateRepository.toString());
        NotificationTemplate fooTemplate = new NotificationTemplate();
        fooTemplate.setEventName("SomethingHappens");
        fooTemplate.setEmailTemplateUrl("SomeEmailTemplateUrl");
        fooTemplate.setSmsTemplateUrl("SomeSmsTemplateUrl");
        fooTemplate.setPushTemplateUrl("SomePushTemplateUrl");
        saved = notificationTemplateRepository.save(fooTemplate);

    }

    @Test
    public void testFindOne()
    {
        NotificationTemplate found = notificationTemplateRepository.findOne(saved.getId());

        assertThat(found).isNotNull();
        System.out.println("Asserted we could find notification template set up in setUp: " + found.getId());

    }

    @Test
    public void testFindByEventName()
    {
        List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findByEventName("SomethingHappens");

        assertThat(notificationTemplates).isNotEmpty();
        System.out.println("Asserted we could find notification template by event name: " + notificationTemplates.size());

    }
}
