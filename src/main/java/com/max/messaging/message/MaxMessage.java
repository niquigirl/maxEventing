package com.max.messaging.message;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by neastman on 3/21/14.
 */
public class MaxMessage implements Serializable
{
    public static final String VERB = "verb";
    private String verb;
    private String language;
    private Date published;
    private String generator;
    private String provider;
    private Map<String, String> displayName;
    private Map<String, String> description;
    private Actor actor;
    private Subject subject;
    private Location location;

    public MaxMessage()
    {
        System.out.println("In MaxMessage constructor");

    }

    public static MaxMessage getInstance(String json) throws JSONException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        MaxMessage message = mapper.readValue(json, MaxMessage.class);

        return message;
    }

    public Properties getMetaProperties()
    {
        Properties metaProperties = new Properties();

        if (getVerb() != null)
            metaProperties.put(VERB, getVerb());

        if (getActor() != null)
        {
            if (getActor().getObjectType() != null)
                metaProperties.put("actor.objectType", getActor().getObjectType());

            if (getActor().getId() != null)
                metaProperties.put("actor.objectId", getActor().getId());
        }

        return metaProperties;
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

    public Map<String, String> getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(Map<String, String> displayName)
    {
        this.displayName = displayName;
    }

    public Map<String, String> getDescription()
    {
        return description;
    }

    public void setDescription(Map<String, String> description)
    {
        this.description = description;
    }

    public Actor getActor()
    {
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
        private String id;
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

        public String getId()
        {
            return id;
        }

        public void setId(String id)
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
}
