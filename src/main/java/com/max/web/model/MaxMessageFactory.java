package com.max.web.model;

import com.max.messaging.MaxTopic;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

/**
 * Build a BadAddressMessage from JSON and a name
 */
public class MaxMessageFactory
{
    Map<MaxTopic, Class<? extends MaxMessage>> builders;

    MaxMessage getInstance(MaxTopic name, String json) throws JSONException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper.readValue(json, builders.get(name));
    }

    public void setBuilders(Map<MaxTopic, Class<? extends MaxMessage>> builders)
    {
        this.builders = builders;
    }
}
