package com.max.messaging.subscribe;

import com.max.messaging.MaxTopic;
import com.max.services.MaxMessageListener;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.jms.TopicSession;

/**
 * This is simply a representation of what subscription details should be
 */
public class SubscriptionDetails
{
    private MaxMessageListener listener;
    private String subscriberName;
    private String filterString;
    private MaxTopic topic;
    private TopicSession topicSession;

    public MaxMessageListener getListener()
    {
        return listener;
    }

    public void setListener(MaxMessageListener listener)
    {
        this.listener = listener;
    }

    public String getSubscriberName()
    {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName)
    {
        this.subscriberName = subscriberName;
    }

    public String getFilterString()
    {
        return filterString;
    }

    public void setFilterString(String filterString)
    {
        this.filterString = filterString;
    }

    public MaxTopic getTopic()
    {
        return topic;
    }

    public void setTopic(MaxTopic topic)
    {
        this.topic = topic;
    }

    public TopicSession getTopicSession()
    {
        return topicSession;
    }

    public void setTopicSession(TopicSession topicSession)
    {
        this.topicSession = topicSession;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("name", getSubscriberName()).append("topic", getTopic()).append("filterString", getFilterString()).build();
    }
}
