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
        System.out.println("In MaxMessage constructor");

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
        return subject;
    }

    public void setSubject(Subject subject)
    {
        this.subject = subject;
    }

    public Location getLocation()
    {
        return location;
    }

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

        public String getDisplayName()
        {
            return displayName;
        }

        public void setDisplayName(String displayName)
        {
            this.displayName = displayName;
        }

        public String getCustomerType()
        {
            return customerType;
        }

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

        public Map<String, String> getMetadata()
        {
            return metadata;
        }

        public void setMetadata(Map<String, String> metadata)
        {
            this.metadata = metadata;
        }
    }
    public static class Location
    {
        private String latitude;
        private String longitude;

        public String getLatitude()
        {
            return latitude;
        }

        public void setLatitude(String latitude)
        {
            this.latitude = latitude;
        }

        public String getLongitude()
        {
            return longitude;
        }

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
