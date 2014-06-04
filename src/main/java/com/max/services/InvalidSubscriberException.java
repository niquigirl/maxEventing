package com.max.services;

/**
 * Indicates a subscriber has failed validation
 */
public class InvalidSubscriberException extends Exception
{
    public InvalidSubscriberException(String s)
    {
        super(s);
    }
}
