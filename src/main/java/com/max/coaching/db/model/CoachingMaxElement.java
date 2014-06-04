package com.max.coaching.db.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Base entity class automatically creating an @Id element named 'id'
 */
@MappedSuperclass
public class CoachingMaxElement implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

}
