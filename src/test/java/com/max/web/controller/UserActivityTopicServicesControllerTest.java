package com.max.web.controller;

import com.max.BaseSpringInjectionUnitTest;
import com.max.db.dao.RemoteSubscriberDao;
import com.max.db.model.RemoteSubscriber;
import com.max.messaging.MaxTopic;
import com.max.web.model.DefaultActivityMessage;
import com.max.web.model.RemoteSubscription;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;


import static org.fest.assertions.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test subscribe and unsubscribe
 */
//@Ignore // Actually runs subscription and stuff, so only use it when you know what you're doing
public class UserActivityTopicServicesControllerTest extends BaseSpringInjectionUnitTest
{
/*
    @Autowired
    UserActivityTopicServicesController services;
*/

    @Autowired
    RemoteSubscriberDao repository;

    /**
     * This tests the sunny-day case of subscribing and unsubscribing, including asserting that:
     * - The JSON as coming in from a web request is properly unmarshalled
     * - When a RemoteSubscription is registered, a RemoteSubscriber record is recorded in the database,
     *   defaulting to autoRegister (on startup)
     * - When a subscriber unsubscribes, the RemoteSubscriber record is changed not to autoRegister
     *
     * @throws Exception
     */
    @Test
    public void testSubscribe() throws Exception
    {
        String subscriberName = "DefaultDITester";
        MaxTopic topic = MaxTopic.DataIntegrity;

        RemoteSubscription subscriber = new RemoteSubscription();
        subscriber.setName(subscriberName);
        subscriber.setRestUrl("http://echo.jsontest.com/message/Hi/success/true");
        subscriber.setAutoRegister(true);
        subscriber.setTopic(topic);
        final String subJson = new ObjectMapper().writeValueAsString(subscriber);

        ResultActions perform = mockMvc.perform(post("/1.0/en/us/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subJson));

        perform.andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        RemoteSubscriber byTopicAndName = repository.findByTopicAndName(topic, subscriberName);
        assertThat(byTopicAndName).isNotNull();
        assertThat(byTopicAndName.getAutoRegister()).isTrue();

        String requestUrl = "/1.0/en/us/DataIntegrity/unsubscribe?subscriber=" + subscriberName;
        perform = mockMvc.perform(get(requestUrl));
        perform.andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        byTopicAndName = repository.findByTopicAndName(topic, subscriberName);
        assertThat(byTopicAndName).isNotNull();
        assertThat(byTopicAndName.getAutoRegister()).isFalse();
    }


    @Test
    public void testPublish() throws Exception
    {
        String subscriberName = "Foo";
        MaxTopic topic = MaxTopic.DataIntegrity;

        DefaultActivityMessage dam = new DefaultActivityMessage();
        dam.setVerb("SomeVerb");
        final DefaultActivityMessage.Actor someActor = new DefaultActivityMessage.Actor();
        someActor.setObjectType("someActorObjectType");
        someActor.setDisplayName("SeeMyType?");
        someActor.setObjectSubtype("Subbie");
        dam.setActor(someActor);
        final DefaultActivityMessage.Subject someSubject = new DefaultActivityMessage.Subject();
        someSubject.setObjectType("SomeSubjectObjectType");
        dam.setObject(someSubject);

        ResultActions perform = mockMvc.perform(post("/1.0/en/us/publish/DataIntegrity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dam.toString()));
    }


    @Test
    public void testUnsubscribe() throws Exception
    {
        MaxTopic topic = MaxTopic.DataIntegrity;
        //services.unsubscribe("", "", "", topic, "DefaultTester");
    }
}
