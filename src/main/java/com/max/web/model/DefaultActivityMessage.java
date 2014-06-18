package com.max.web.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.*;

/**
 * POJO Defining the JSON structure of a Message
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultActivityMessage extends MaxMessage<DefaultActivityMessage, MaxMessage.Subject, MaxMessage.Actor>
{

    public DefaultActivityMessage()
    {
        super();
    }

    /**
     * This method will generate a test instance and leave no field null
     *
     * @return {@code ActivityMessage}
     */
    @Override
    public DefaultActivityMessage generateTestInstance()
    {
        DefaultActivityMessage message = new DefaultActivityMessage();
        final Subject testSubject = new Subject();
        testSubject.setObjectType("TestSubjectObjectType");

        final Actor testActor = new Actor();
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
}
