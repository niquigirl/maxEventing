package com.max.messaging.subscribe.impl;

import com.max.BaseMockUnitTest;
import com.max.web.model.HandlerResults;
import com.max.web.model.MaxMessage;
import com.max.services.impl.RemoteSubscriberFacade;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Validate RemoteSubscriberFacade functionality
 */
public class RemoteSubscriberFacadeTest extends BaseMockUnitTest
{
    @Test
    public void testOnMessage() throws Exception
    {
        final MaxMessage maxMessage = new MaxMessage();
        maxMessage.setVerb("TestVerb");
        final MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setObjectType("TestActorType");
        maxMessage.setActor(actor);

        final RemoteSubscriberFacade remoteSubscriberFacade = mock(RemoteSubscriberFacade.class);
        when(remoteSubscriberFacade.getTestUrl()).thenReturn("http://echo.jsontest.com/message/Hi/success/true");
        when(remoteSubscriberFacade.onTest(any(MaxMessage.class))).thenCallRealMethod();
        when(remoteSubscriberFacade.getLogger()).thenReturn(mock(Logger.class));
        final HandlerResults handlerResults = remoteSubscriberFacade.onTest(MaxMessage.generateTestInstance());

        assertThat(handlerResults).isNotNull();
        assertThat(handlerResults.getMessage()).isEqualToIgnoringCase("Hi");
        assertThat(handlerResults.isSuccess()).isTrue();
        System.out.println("Asserted RemoteSubscriberFacade will consume a JSON URL and Deserialize results");
    }
}
