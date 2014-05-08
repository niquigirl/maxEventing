package com.max.coaching.db.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Customer entity bean for MaxReporting (Exigo) customers
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "TaskTemplate")
public class TaskTemplate extends CoachingMaxElement
{
    private String taskClass;
    private String descriptionKey;
    private String url;
    private String detailUrl;
    private String formUrl;

    public String getDescriptionKey()
    {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey)
    {
        this.descriptionKey = descriptionKey;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getDetailUrl()
    {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl)
    {
        this.detailUrl = detailUrl;
    }

    public String getFormUrl()
    {
        return formUrl;
    }

    public void setFormUrl(String formUrl)
    {
        this.formUrl = formUrl;
    }

    public String getTaskClass()
    {
        return taskClass;
    }

    public void setTaskClass(String taskClass)
    {
        this.taskClass = taskClass;
    }
}
