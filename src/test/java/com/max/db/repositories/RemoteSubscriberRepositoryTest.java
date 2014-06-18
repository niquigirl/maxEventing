package com.max.db.repositories;

import com.max.BaseSpringInjectionUnitTest;
import com.max.db.model.RemoteSubscriber;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by neastman on 6/17/14.
 */
public class RemoteSubscriberRepositoryTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    RemoteSubscriberRepository repository;

    @Test
    public void testGetAll()
    {
        final List<RemoteSubscriber> all = repository.findAll();

        assertThat(all).isNotEmpty();
    }
}
