package com.max.web.services;

import org.apache.axis.utils.StringUtils;

/**
 * Common functionality for Web Services
 */
public abstract class MaxWebService
{
    /**
     * Convert a param value to int, throwing a {@code ParamsValidationException} if a value cannot be converted
     *
     * @param paramName {@code String}
     * @param value {@code String}
     * @return {@code Integer} or {@code null} if value is null or empty
     * @throws ParamsValidationException
     */
    Integer getInt(String paramName, String value) throws ParamsValidationException
    {
        if (StringUtils.isEmpty(value))
            return null;

        // Make sure numerics are numeric
        try
        {
            return Integer.valueOf(value);
        }
        catch (NumberFormatException e)
        {
            throw new ParamsValidationException("Non-numeric param value received where numeric was expected. Error param: " + paramName + "=" + value);
        }
    }

}
