package com.max.web.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Properties;

/**
 * POJO Defining the JSON structure of a Message
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultActivityMessage extends JsonData
{
    @JsonIgnore
    public static final String VERB = "verb";
    @JsonIgnore
    public static final Object ACTOR_OBJECT_TYPE = "actorObjectType";
    @JsonIgnore
    public static final Object SUBJECT_OBJECT_TYPE = "subjectObjectType";

    private Actor actor;
    private Subject object;
    private String verb;

    public static DefaultActivityMessage getInstance(String json) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper.readValue(json, DefaultActivityMessage.class);
    }

    /**
     * From the Message provided, create the top-level properties that can be used for subscribers to filter
     *
     * @return {@code Properties}
     */
    public Properties getMetaPropertiesForPublish()
    {
        Properties metaProperties = new Properties();

        if (getVerb() != null)
            metaProperties.put(VERB, getVerb());

        if (getActor() != null)
        {
            if (getActor().getObjectType() != null)
                metaProperties.put(ACTOR_OBJECT_TYPE, getActor().getObjectType());
        }

        if (getObject() != null)
        {
            if (getObject().getObjectType() != null)
                metaProperties.put(SUBJECT_OBJECT_TYPE, getObject().getObjectType());
        }

        return metaProperties;
    }

    public Actor getActor()
    {
        return actor;
    }

    public void setActor(Actor actor)
    {
        this.actor = actor;
    }

    public Subject getObject()
    {
        return object;
    }

    public void setObject(Subject object)
    {
        this.object = object;
    }

    public String getVerb()
    {
        return verb;
    }

    public void setVerb(String verb)
    {
        this.verb = verb;
    }

    public static class Actor
    {
        private String objectType;
        private String displayName;
        private String objectSubtype;

        public String getObjectType()
        {
            return objectType;
        }

        public void setObjectType(String objectType)
        {
            this.objectType = objectType;
        }

        public String getDisplayName()
        {
            return displayName;
        }

        public void setDisplayName(String displayName)
        {
            this.displayName = displayName;
        }

        /**
         * An object subtype is intended to provide a simple hierarchy to describe an actor, such
         * as an objectType of "User" with an objectSubtype of "Associate"
         */
        public String getObjectSubtype()
        {
            return objectSubtype;
        }

        public void setObjectSubtype(String objectSubtype)
        {
            this.objectSubtype = objectSubtype;
        }
    }


    public static class Subject
    {
        private String objectType;

        public Subject(){}
        public String getObjectType()
        {
            return objectType;
        }

        public void setObjectType(String objectType)
        {
            this.objectType = objectType;
        }

    }
}
