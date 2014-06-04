package com.max.web.controller;

/**
 * Categorization of an error with Web Service request params
 */
public class ParamsValidationException extends Exception
{
    public ParamsValidationException(String message)
    {
        super(message);
    }
}
