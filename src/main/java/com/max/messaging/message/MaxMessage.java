package com.max.messaging.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * POJO Defining the JSON structure of a Message
 */
public class MaxMessage implements Serializable
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

    private String verb;
    private String language = "en";
    private Date published;
    private String generator;
    private String provider;
    public Actor actor;
    public Subject subject;
    private Location location;

    public MaxMessage()
    {
        super();
    }

    @JsonIgnore
    public static MaxMessage getInstance(String json) throws JSONException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper.readValue(json, MaxMessage.class);
    }

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

    @SuppressWarnings("unused")
    public Date getPublished()
    {
        return published;
    }

    @SuppressWarnings("unused")
    public void setPublished(Date published)
    {
        this.published = published;
    }

    @SuppressWarnings("unused")
    public String getGenerator()
    {
        return generator;
    }

    @SuppressWarnings("unused")
    public void setGenerator(String generator)
    {
        this.generator = generator;
    }

    @SuppressWarnings("unused")
    public String getProvider()
    {
        return provider;
    }

    @SuppressWarnings("unused")
    public void setProvider(String provider)
    {
        this.provider = provider;
    }

    public Actor getActor()
    {
        if (actor == null)
            actor = new Actor();
        return actor;
    }

    public void setActor(Actor actor)
    {
        this.actor = actor;
    }

    public Subject getSubject()
    {
        if (subject == null)
            subject = new Subject();
        return subject;
    }

    public void setSubject(Subject subject)
    {
        this.subject = subject;
    }

    @SuppressWarnings("unused")
    public Location getLocation()
    {
        return location;
    }

    @SuppressWarnings("unused")
    public void setLocation(Location location)
    {
        this.location = location;
    }

    public static class Actor
    {
        private String objectType;
        public Integer id;
        private String displayName;
        private String customerType;

        public String getObjectType()
        {
            return objectType;
        }

        public void setObjectType(String objectType)
        {
            this.objectType = objectType;
        }

        public Integer getId()
        {
            return id;
        }

        public void setId(Integer id)
        {
            this.id = id;
        }

        @SuppressWarnings("unused")
        public String getDisplayName()
        {
            return displayName;
        }

        public void setDisplayName(String displayName)
        {
            this.displayName = displayName;
        }

        @SuppressWarnings("unused")
        public String getCustomerType()
        {
            return customerType;
        }

        @SuppressWarnings("unused")
        public void setCustomerType(String customerType)
        {
            this.customerType = customerType;
        }
    }

    public static class Subject
    {
        private Integer id;
        private String objectType;
        private Map<String, String> metadata;

        public Integer getId()
        {
            return id;
        }

        public void setId(Integer id)
        {
            this.id = id;
        }

        public String getObjectType()
        {
            return objectType;
        }

        public void setObjectType(String objectType)
        {
            this.objectType = objectType;
        }

        @SuppressWarnings("unused")
        public Map<String, String> getMetadata()
        {
            return metadata;
        }

        @SuppressWarnings("unused")
        public void setMetadata(Map<String, String> metadata)
        {
            this.metadata = metadata;
        }
    }
    public static class Location
    {
        private String latitude;
        private String longitude;

        @SuppressWarnings("unused")
        public String getLatitude()
        {
            return latitude;
        }

        @SuppressWarnings("unused")
        public void setLatitude(String latitude)
        {
            this.latitude = latitude;
        }

        @SuppressWarnings("unused")
        public String getLongitude()
        {
            return longitude;
        }

        @SuppressWarnings("unused")
        public void setLongitude(String longitude)
        {
            this.longitude = longitude;
        }
    }


    @Override
    public String toString()
    {
        return new JSONObject(this).toString();
    }
}
