package com.max.web.subscribers;

import com.max.BaseSpringInjectionUnitTest;
import com.max.web.model.MaxMessage;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by neastman on 5/29/14.
 */
public class MessagingServicesTesterControllerTest extends BaseSpringInjectionUnitTest
{
    @Test
    public void testBasicREST() throws Exception
    {
        System.out.println("Testing basic REST plumbing, sending a Message to /testRest");
        String requestUrl = "/testRest";
        MaxMessage testMessage = new MaxMessage();
        testMessage.setVerb("Test");
        final MaxMessage.Subject object = new MaxMessage.Subject();
        object.setObjectType("SomeSubjectType");
        final MaxMessage.Actor actor = new MaxMessage.Actor();
        actor.setObjectType("SomeActorType");

        testMessage.setObject(object);
        testMessage.setActor(actor);

        ResultActions perform = mockMvc.perform(post(requestUrl)
                .contentType(APPLICATION_JSON_UTF8)
                .content(testMessage.toString()));

        perform
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }
}
