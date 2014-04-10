package com.max.web.model;

import com.max.coaching.db.model.CoachingMaxElement;

/**
 * Define the behavior of DataModelBeans
 *
 * A DataModelBean is intended to package data to be delivered via Web Services
 */
public interface DataModelBean<T extends CoachingMaxElement>
{
    public boolean loadFrom(T coachingMaxElement, String lang);
}
