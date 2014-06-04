package com.max.messaging.subscribe;

/**
 * This is simply a representation of what subscription details should be
 */
public class SubscriptionDetails
{
    private MaxMessageListener listener;
    private String subscriberName;
    private String filterString;

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
}
