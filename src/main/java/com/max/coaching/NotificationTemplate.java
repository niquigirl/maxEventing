package com.max.coaching;

import javax.persistence.*;

/**
 * Customer entity bean for MaxReporting (Exigo) customers
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "NotificationTemplate")
public class NotificationTemplate extends CoachingMaxElement
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

    private String eventName;
    private String recipientType;
    private String emailTemplateUrl;
    private String smsTemplateUrl;
    private String pushTemplateUrl;

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getRecipientType()
    {
        return recipientType;
    }

    public void setRecipientType(String recipientType)
    {
        this.recipientType = recipientType;
    }

    public String getEmailTemplateUrl()
    {
        return emailTemplateUrl;
    }

    public void setEmailTemplateUrl(String emailTemplateUrl)
    {
        this.emailTemplateUrl = emailTemplateUrl;
    }

    public String getSmsTemplateUrl()
    {
        return smsTemplateUrl;
    }

    public void setSmsTemplateUrl(String smsTemplateUrl)
    {
        this.smsTemplateUrl = smsTemplateUrl;
    }

    public String getPushTemplateUrl()
    {
        return pushTemplateUrl;
    }

    public void setPushTemplateUrl(String pushTemplateUrl)
    {
        this.pushTemplateUrl = pushTemplateUrl;
    }
}
