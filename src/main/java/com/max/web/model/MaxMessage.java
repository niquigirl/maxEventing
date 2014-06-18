package com.max.web.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Contract Defining the behavior of a Message
 */
@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MaxMessage<M extends MaxMessage, S extends MaxMessage.Subject, A extends MaxMessage.Actor> extends JsonData
{
    @JsonIgnore
    public static final String VERB = "verb";
    @JsonIgnore
    public static final Object ACTOR_OBJECT_TYPE = "actor.objectType";
    @JsonIgnore
    public static final Object ACTOR_ID = "actor.objectId";
    @JsonIgnore
    public static final Object SUBJECT_OBJECT_TYPE = "subject.objectType";
    @JsonIgnore
    public static final Object SUBJECT_ID = "subject.objectId";

    private A actor;
    private S object;

    private String verb;
    private Date published;
    private String language = "en";
    private String generator;
    private String provider;
    private Location location;


    public static DefaultActivityMessage getInstance(String json) throws JSONException, IOException
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

    /**
     * This method will generate a test instance and leave no field null
     *
     * @return {@code ActivityMessage}
     */
    public abstract M generateTestInstance();

    public String getVerb()
    {
        return verb;
    }

    public void setVerb(String verb)
    {
        this.verb = verb;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public Date getPublished()
    {
        return published;
    }

    public void setPublished(Date published)
    {
        this.published = published;
    }

    public String getGenerator()
    {
        return generator;
    }

    public void setGenerator(String generator)
    {
        this.generator = generator;
    }

    public String getProvider()
    {
        return provider;
    }

    public void setProvider(String provider)
    {
        this.provider = provider;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }


    public S getObject()
    {
        return object;
    }

    public void setObject(S object)
    {
        this.object = object;
    }

    public A getActor()
    {
        return actor;
    }

    public void setActor(A actor)
    {
        this.actor = actor;
    }



    /**
     * <p>Construct to maintain data that makes up the Actor: Assumed to be the one performing the {@link #verb}</p>
     */
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

    public static class Location
    {
        private String lat;
        private String lng;

        public String getLat()
        {
            return lat;
        }

        public void setLat(String lat)
        {
            this.lat = lat;
        }

        public String getLng()
        {
            return lng;
        }

        public void setLng(String lng)
        {
            this.lng = lng;
        }
    }
}
