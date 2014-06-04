package com.max.web.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * POJO Defining the JSON structure of a Message
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
    public static final int TEST_SUBJECT_ID = 535353;
    public static final int TEST_ACTOR_ID = 454545;

    private String verb;
    private Subject object;
    private Actor actor;
    private Date published;
    private String language = "en";
    private String generator;
    private String provider;
    private Location location;

    public MaxMessage()
    {
        super();
    }

    /**
     * This method will generate a test instance and leave no field null
     *
     * @return {@code MaxMessage}
     */
    public static MaxMessage generateTestInstance()
    {
        MaxMessage message = new MaxMessage();
        final Subject testSubject = new Subject();
        testSubject.setObjectType("TestSubjectObjectType");
        testSubject.setId(TEST_SUBJECT_ID);
        final HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("SomeTestProperty", "SomeTestPropertyValue");
        testSubject.setProperties(properties);

        final Actor testActor = new Actor();
        testActor.setId(TEST_ACTOR_ID);
        testActor.setObjectType("TestActorType");
        testActor.setObjectSubtype("TestActorSubType");
        testActor.setDisplayName("TestActorDisplayName");

        message.setVerb("TestVerb");
        message.setGenerator("TestGenerator");
        message.setLanguage("TestLang");
        message.setProvider("TestProvider");
        message.setPublished(new Date());
        message.setObject(testSubject);
        message.setActor(testActor);

        return message;
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

    public Subject getObject()
    {
        if (object == null)
            object = new Subject();
        return object;
    }

    public void setObject(Subject object)
    {
        this.object = object;
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

    /**
     * <p>Construct to maintain data that makes up the Actor: Assumed to be the one performing the {@link #verb}</p>
     */
    public static class Actor
    {
        private Integer id;
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
        /**
         * An object subtype is intended to provide a simple hierarchy to describe an actor, such
         * as an objectType of "User" with an objectSubtype of "Associate"
         */
        public String getObjectSubtype()
        {
            return objectSubtype;
        }

        @SuppressWarnings("unused")
        public void setObjectSubtype(String objectSubtype)
        {
            this.objectSubtype = objectSubtype;
        }
    }

    public static class Subject
    {
        private Integer id;
        private String objectType;
        private Map<String, String> properties;

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
        public Map<String, String> getProperties()
        {
            return properties;
        }

        @SuppressWarnings("unused")
        public void setProperties(Map<String, String> properties)
        {
            this.properties = properties;
        }
    }
    public static class Location
    {
        private String lat;
        private String lng;

        @SuppressWarnings("unused")
        public String getLat()
        {
            return lat;
        }

        @SuppressWarnings("unused")
        public void setLat(String lat)
        {
            this.lat = lat;
        }

        @SuppressWarnings("unused")
        public String getLng()
        {
            return lng;
        }

        @SuppressWarnings("unused")
        public void setLng(String lng)
        {
            this.lng = lng;
        }
    }


    @Override
    public String toString()
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.writeValueAsString(this);
        }
        catch (IOException e)
        {
            return "INVALID JSON";
        }
    }

}
