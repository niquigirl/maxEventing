package com.max.web.model;

/**
 * Define the construct to allow a remote subscriber to return results
 */
public class HandlerResults extends JsonData
{
    private String message;
    private Boolean success;

    public HandlerResults() {}

    public HandlerResults(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }
}
