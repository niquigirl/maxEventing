package com.max.messaging.message;

import java.util.Properties;

/**
 * Created by neastman on 3/31/14.
 */
public class MaxMessageUtils
{
    public static Properties getMetaProperties(MaxMessage message)
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
        
        if (message.getSubject() != null)
        {
            if (message.getSubject().getObjectType() != null)
                metaProperties.put(MaxMessage.SUBJECT_OBJECT_TYPE, message.getSubject().getObjectType());

            if (message.getSubject().getId() != null)
                metaProperties.put(MaxMessage.SUBJECT_ID, message.getSubject().getId());
        }

        return metaProperties;
    }

}
