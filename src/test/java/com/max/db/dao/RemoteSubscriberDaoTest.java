package com.max.db.dao;

import com.max.BaseSpringInjectionUnitTest;
import com.max.db.model.RemoteSubscriber;
import com.max.messaging.MaxTopic;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 *
 */
public class RemoteSubscriberDaoTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    RemoteSubscriberDao dao;

    @Test
    public void verifyGetAllForAutoRegister()
    {
        dao.findByAutoRegister(true);
    }

    @Test
    public void verifySave()
    {
        RemoteSubscriber subscriber = new RemoteSubscriber(null, "TestName", "TestRestUrl", "TestFilter", MaxTopic.DataIntegrity, 10000, true);

        dao.save(subscriber);

        final RemoteSubscriber saved = dao.findByTopicAndName(MaxTopic.DataIntegrity, "TestName");
        assertThat(saved).isNotNull();

        assertThat(dao.delete(saved)).isTrue();
    }

    @Test
    public void testFindByTopicAndName()
    {
        final RemoteSubscriber testSubscriber = dao.findByTopicAndName(MaxTopic.DataIntegrity, "TestSubscriber");

        System.out.println("Is null? " + testSubscriber);
    }
}
