package com.max.db.repositories;

import com.max.BaseSpringInjectionUnitTest;
import com.max.db.dao.RemoteSubscriberDao;
import com.max.db.model.RemoteSubscriber;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Validate injection
 */
public class RemoteSubscriberRepositoryTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    RemoteSubscriberDao repository;

    @Test
    public void testGetAll()
    {
        final Collection<RemoteSubscriber> all = repository.findAll();

        assertThat(all).isNotEmpty();

        System.out.println(all);
    }
}
