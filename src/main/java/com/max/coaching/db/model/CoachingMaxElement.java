package com.max.coaching.db.model;

import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by neastman on 3/27/14.
 */
@MappedSuperclass
public class CoachingMaxElement implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return new JSONObject(this).toString();
    }
}
