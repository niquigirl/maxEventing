package com.max.messaging.subscribe;

/**
 * Indicates there was some error with managing a topic
 */
public class TopicManagementException extends Exception
{
    public TopicManagementException(String message)
    {
        super(message);
    }

    public TopicManagementException(String message, Exception cause)
    {
        super(message, cause);
    }
}
