package com.max.web.subscribers;

import com.max.BaseSpringInjectionUnitTest;
import com.max.web.model.MaxMessage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test the AutoTaskFlow remote subscriber
 */
public class AutoTaskFlowSubscriberControllerTest extends BaseSpringInjectionUnitTest
{
    @Test
    public void testOnMessage() throws Exception
    {
        final MaxMessage maxMessage = MaxMessage.generateTestInstance();

        ResultActions perform = mockMvc.perform(post("/1.0/en/us/testAutoTaskFlow")
                .contentType(APPLICATION_JSON_UTF8)
                .content(maxMessage.toString()));

        perform
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(maxMessage.getGenerator())))
                .andExpect(jsonPath("$.success", is(true)));

        //System.out.println("Asserted " + requestUrl);
    }
}
