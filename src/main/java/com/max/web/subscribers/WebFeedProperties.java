package com.max.web.subscribers;

/**
 * Encompass all web feed properties
 */
public class WebFeedProperties
{
    private String webChannel;
    private String root;
    private String publishKey;
    private String subscribeKey;

    public String getWebChannel()
    {
        return webChannel;
    }

    public void setWebChannel(String webChannel)
    {
        this.webChannel = webChannel;
    }

    public String getRoot()
    {
        return root;
    }

    public void setRoot(String root)
    {
        this.root = root;
    }

    public String getPublishKey()
    {
        return publishKey;
    }

    public void setPublishKey(String publishKey)
    {
        this.publishKey = publishKey;
    }

    public String getSubscribeKey()
    {
        return subscribeKey;
    }

    public void setSubscribeKey(String subscribeKey)
    {
        this.subscribeKey = subscribeKey;
    }
}
