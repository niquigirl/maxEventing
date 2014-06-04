package com.max.messaging.subscribe;

import com.max.web.model.MaxMessage;

import java.util.Properties;

/**
 * This class creates a list of Properties to be used as metadata on the queue that can be used for filtering.
 * If there are any attributes of {@link com.max.web.model.MaxMessage} that should be filter'able, add that here
 */
public class UserActivityTopicFilterUtil
{
    /**
     * From the Message provided, create the top-level properties that can be used for subscribers to filter
     *
     * @param message {@link com.max.web.model.MaxMessage} Message from which to obtain values for the
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
