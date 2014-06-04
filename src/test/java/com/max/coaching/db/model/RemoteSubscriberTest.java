package com.max.coaching.db.model;

import com.max.BaseSpringInjectionUnitTest;
import com.max.coaching.db.repositories.RemoteSubscriberRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * This method assumes the existence of a "DefaultTester" listener that points to a REST service
 * exposed by this code-base.  In the future, we probably want to have that URL configurable, but
 * should probably always want to have that service available
 */
public class RemoteSubscriberTest extends BaseSpringInjectionUnitTest
{
    @Autowired
    RemoteSubscriberRepository subscriberRepository;

    @Test
    public void proveAssociation()
    {
        final RemoteSubscriber defaultTester = subscriberRepository.findByName("DefaultTester");

        assertThat(defaultTester.getRemoteSubscriptionCriteria()).isNotEmpty();
    }
}
