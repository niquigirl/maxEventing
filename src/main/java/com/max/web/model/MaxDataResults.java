package com.max.web.model;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.*;

/**
 * JSON POJO for Data results.  MaxDataResults JSON will follow the object hierarchy of this class
 */
public class MaxDataResults<T extends DataModelBean>
{
    private int dataSize;
    private List<T> data;
    private String requestName;
    private List<Map.Entry<String, String>> requestParams;
    private List<String> errors;

    @SafeVarargs
    public MaxDataResults(String requestName, Map.Entry<String, String>... requestParams)
    {
        this.requestName = requestName;
        this.requestParams = new LinkedList<>();
        this.requestParams.addAll(Arrays.asList(requestParams));
    }

    /**
     * Add an error.  Empty String or null will be ignored
     *
     * @param errorMessage {@code String}
     */
    @SuppressWarnings("unused")
    public void addError(String errorMessage)
    {
        if (StringUtils.isNotBlank(errorMessage))
        {
            if (errors == null)
                errors = new LinkedList<>();

            errors.add(errorMessage);
        }
    }

    /**
     * Add a request param. Null or empty paramNames are ignored
     *
     * @param paramName {@code String}
     * @param paramValue {@code String}
     */
    @SuppressWarnings("unused")
    public void addRequestParam(String paramName, Object paramValue)
    {
        if (StringUtils.isBlank(paramName))
            return;

        if (requestParams == null)
            requestParams = new LinkedList<>();

        requestParams.add(new AbstractMap.SimpleEntry<>(paramName, paramValue == null ? "null" : paramValue.toString()));
    }

    /**
     * Data retrieved
     */
    @SuppressWarnings("unused")
    public List<T> getData()
    {
        return data;
    }

    public void setData(List<T> data)
    {
        this.data = data;
        if (this.data != null)
            this.dataSize = this.data.size();
        else
            this.dataSize = 0;
    }

    /**
     * Add a data record
     *
     * @param data {@code T}
     */
    @SuppressWarnings("unused")
    public void addData(T data)
    {
        if (data == null)
            return;

        if (this.data == null)
            this.data = new LinkedList<>();

        this.data.add(data);
        this.dataSize = this.data.size();
    }

    /**
     * List of errors in sentence-format.  The non-existence of errors indicates success
     */
    @SuppressWarnings("unused")
    public List<String> getErrors()
    {
        return errors;
    }

    @SuppressWarnings("unused")
    public void setErrors(List<String> errors)
    {
        this.errors = errors;
    }

    /**
     * The number of data records sent
     *
     * @return {@code int} 0 if no records
     */
    @SuppressWarnings("unused")
    public int getDataSize()
    {
        return dataSize;
    }

    @SuppressWarnings("unused")
    public void setDataSize(int dataSize)
    {
        this.dataSize = dataSize;
    }
    /**
     * Request params received in the service request
     */
    @SuppressWarnings("unused")
    public List<Map.Entry<String, String>> getRequestParams()
    {
        return requestParams;
    }

    /**
     * Names the HTTP request name (e.g. 'friendsAndFamily')
     */
    @SuppressWarnings("unused")
    public String getRequestName()
    {
        return requestName;
    }

    @SuppressWarnings("unused")
    public void setRequestName(String requestName)
    {
        this.requestName = requestName;
    }


    @Override
    public String toString()
    {
        return new JSONObject(this).toString();
    }

}
