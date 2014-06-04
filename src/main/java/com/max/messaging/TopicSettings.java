package com.max.messaging;


/**
 * POJO to hold all settings for each specific Topic
 */
public class TopicSettings
{
    public String qpidIcf;
    private String connectionFactoryNamePrefix;
    private String connectionFactoryName;
    private String topicNamePrefix;
    private String topicName;
    private String userName;
    private String password;
    private String carbonClientId;
    private String carbonVirtualHostName;
    private String carbonDefaultHostname;
    private String carbonDefaultPort;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCarbonClientId()
    {
        return carbonClientId;
    }

    public void setCarbonClientId(String carbonClientId)
    {
        this.carbonClientId = carbonClientId;
    }

    public String getCarbonVirtualHostName()
    {
        return carbonVirtualHostName;
    }

    public void setCarbonVirtualHostName(String carbonVirtualHostName)
    {
        this.carbonVirtualHostName = carbonVirtualHostName;
    }

    public String getCarbonDefaultHostname()
    {
        return carbonDefaultHostname;
    }

    public void setCarbonDefaultHostname(String carbonDefaultHostname)
    {
        this.carbonDefaultHostname = carbonDefaultHostname;
    }

    public String getCarbonDefaultPort()
    {
        return carbonDefaultPort;
    }

    public void setCarbonDefaultPort(String carbonDefaultPort)
    {
        this.carbonDefaultPort = carbonDefaultPort;
    }

    public String getConnectionFactoryNamePrefix()
    {
        return connectionFactoryNamePrefix;
    }

    public void setConnectionFactoryNamePrefix(String connectionFactoryNamePrefix)
    {
        this.connectionFactoryNamePrefix = connectionFactoryNamePrefix;
    }

    public String getConnectionFactoryName()
    {
        return connectionFactoryName;
    }

    public void setConnectionFactoryName(String connectionFactoryName)
    {
        this.connectionFactoryName = connectionFactoryName;
    }

    public String getQpidIcf()
    {
        return qpidIcf;
    }

    public void setQpidIcf(String qpidIcf)
    {
        this.qpidIcf = qpidIcf;
    }

    public String getTopicNamePrefix()
    {
        return topicNamePrefix;
    }

    public void setTopicNamePrefix(String topicNamePrefix)
    {
        this.topicNamePrefix = topicNamePrefix;
    }

    public String getTopicName()
    {
        return topicName;
    }

    public void setTopicName(String topicName)
    {
        this.topicName = topicName;
    }
}
