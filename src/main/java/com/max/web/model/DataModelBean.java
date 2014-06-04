package com.max.web.model;

import com.max.coaching.db.model.CoachingMaxElement;

/**
 * Define the behavior of DataModelBeans
 *
 * A DataModelBean is intended to package data to be delivered via Web Services
 */
public abstract class DataModelBean<T extends CoachingMaxElement> extends JsonData
{
    public abstract boolean loadFrom(T coachingMaxElement, String lang);
}
