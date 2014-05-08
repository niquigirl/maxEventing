package com.max.messaging.message;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Properties;

/**
 * This class provides methods around creating the properties on which subscribers can filter
 */
public class TopicFilterProperties
{
    String verb;
    String actorId;
    String actorObjectType;
    @JsonProperty("ObjectId")
    String subjectId;
    @JsonProperty("ObjectType")
    String subjectObjectType;
    String targetId;

    /**
     * From the Message provided, create the top-level properties that can be used for subscribers to filter
     *
     * @param message {@link com.max.messaging.message.MaxMessage} Message from which to obtain values for the
     *                                                            properties exposed for Subscribers to filter
     * @return {@code Properties}
     */
    public static Properties getMetaPropertiesForPublish(MaxMessage message)
    {
        Properties metaProperties = new Properties();

        if (message.getVerb() != null)
            metaProperties.put(MaxMessage.VERB, message.getVerb());

        if (message.getActor() != null)
        {
            if (message.getActor().getObjectType() != null)
                metaProperties.put(MaxMessage.ACTOR_OBJECT_TYPE, message.getActor().getObjectType());

            if (message.getActor().getId() != null)
                metaProperties.put(MaxMessage.ACTOR_ID, message.getActor().getId());
        }
        
        if (message.getObject() != null)
        {
            if (message.getObject().getObjectType() != null)
                metaProperties.put(MaxMessage.SUBJECT_OBJECT_TYPE, message.getObject().getObjectType());

            if (message.getObject().getId() != null)
                metaProperties.put(MaxMessage.SUBJECT_ID, message.getObject().getId());
        }

        return metaProperties;
    }

}
