package com.max.messaging.subscribe.impl;

import java.util.Map;

/**
 * POJO to centrally maintain the mapping between Events and Tasks
 */
public class EventTaskMapping
{
    Map<String, String> eventTaskMap;

    public String getTaskName(String eventName)
    {
        return eventTaskMap.get(eventName);
    }
    public void setEventTaskMap(Map<String, String> eventTaskMap)
    {
        this.eventTaskMap = eventTaskMap;
    }
}
