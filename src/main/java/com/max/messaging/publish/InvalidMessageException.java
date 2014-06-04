package com.max.messaging.publish;

/**
 * Something is invalid about a message about to be published
 */
public class InvalidMessageException extends Exception
{
    public InvalidMessageException(String message)
    {
        super(message);
    }
}
