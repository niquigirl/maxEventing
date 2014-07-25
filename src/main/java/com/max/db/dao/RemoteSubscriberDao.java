package com.max.db.dao;

import com.max.db.model.RemoteSubscriber;
import com.max.messaging.MaxTopic;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Access/manage RemoteSubscriber data
 */
@Repository
public class RemoteSubscriberDao
{
    Logger log = Logger.getLogger(RemoteSubscriberDao.class);

    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void showWhereYouAre()
    {
        final Collection<RemoteSubscriber> all = findAll();
        log.debug("Inialized RemoteSubscriberDao, all subscribers: " + (all == null ? "Null" : all.size()));
    }

    @Transactional
    public boolean save(RemoteSubscriber subscriber)
    {
        String getSubscribersSql;

        if (subscriber.getId() == null)
        {
            getSubscribersSql = "insert into RemoteSubscriber(restUrl, timeout, name, autoRegister, topic, filterString) " +
                "values (?, ?, ?, ?, ?, ?)";
        }
        else
        {
            getSubscribersSql = "update RemoteSubscriber set restUrl=?, timeout=?, name=?, autoRegister=?, topic=?, filterString=? where id=?";
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getSubscribersPS = connection.prepareStatement(getSubscribersSql))
        {
            getSubscribersPS.setString(1, subscriber.getRestUrl());
            getSubscribersPS.setInt(2, subscriber.getTimeout());
            getSubscribersPS.setString(3, subscriber.getName());
            getSubscribersPS.setInt(4, subscriber.getAutoRegister() ? 1 : 0); // TODO: test this
            getSubscribersPS.setString(5, subscriber.getTopic().name());
            getSubscribersPS.setString(6, subscriber.getFilterString());

            if (subscriber.getId() != null)
                getSubscribersPS.setInt(7, subscriber.getId());

            final int i = getSubscribersPS.executeUpdate();
            return i == 1;
        }
        catch (SQLException | IllegalArgumentException e)
        {
            log.error(e);
            e.printStackTrace();
        }

        return false;
    }

    @Transactional
    public Collection<RemoteSubscriber> findByAutoRegister(boolean b)
    {
        String getSubscribersSql = "select id, restUrl, timeout, name, autoRegister, topic, filterString from RemoteSubscriber " +
                "where autoRegister = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getSubscribersPS = connection.prepareStatement(getSubscribersSql))
        {
            getSubscribersPS.setBoolean(1, b);

            try (final ResultSet resultSet = getSubscribersPS.executeQuery())
            {
                Collection<RemoteSubscriber> results = new LinkedList<>();
                while (resultSet.next())
                {
                    int paramIndex = 0;
                    Integer id = resultSet.getInt(++paramIndex);
                    String restUrl = resultSet.getString(++paramIndex);
                    Integer timeout = resultSet.getInt(++paramIndex);
                    String name = resultSet.getString(++paramIndex);
                    Boolean autoRegiser = resultSet.getBoolean(++paramIndex);
                    String topic = resultSet.getString(++paramIndex);
                    String filterString = resultSet.getString(++paramIndex);

                    results.add(new RemoteSubscriber(id, name, restUrl, filterString, MaxTopic.valueOf(topic), timeout, autoRegiser));
                }

                return results;
            }
        }
        catch (SQLException | IllegalArgumentException e)
        {
            //log.error("Failure attempting to get Customers for fixing pay type", e);
        }

        return Collections.emptyList();
    }

    @Transactional
    public RemoteSubscriber findByTopicAndName(MaxTopic topic, String subscriberName)
    {
        String getSubscribersSql = "select id, restUrl, timeout, name, autoRegister, topic, filterString from RemoteSubscriber " +
                "where topic = ? and name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getSubscribersPS = connection.prepareStatement(getSubscribersSql))
        {
            getSubscribersPS.setString(1, topic.name());
            getSubscribersPS.setString(2, subscriberName);

            try (final ResultSet resultSet = getSubscribersPS.executeQuery())
            {
                if (resultSet.next())
                {
                    int paramIndex = 0;
                    Integer id = resultSet.getInt(++paramIndex);
                    String restUrl = resultSet.getString(++paramIndex);
                    Integer timeout = resultSet.getInt(++paramIndex);
                    String name = resultSet.getString(++paramIndex);
                    Boolean autoRegiser = resultSet.getBoolean(++paramIndex);
                    String topicS = resultSet.getString(++paramIndex);
                    String filterString = resultSet.getString(++paramIndex);

                    return new RemoteSubscriber(id, name, restUrl, filterString, MaxTopic.valueOf(topicS), timeout, autoRegiser);
                }

                return null;
            }
        }
        catch (SQLException | IllegalArgumentException e)
        {
            //log.error("Failure attempting to get Customers for fixing pay type", e);
        }

        return null;
    }

    @Transactional
    public Collection<RemoteSubscriber> findAll()
    {
        String getSubscribersSql = "select id, restUrl, timeout, name, autoRegister, topic, filterString from RemoteSubscriber ";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getSubscribersPS = connection.prepareStatement(getSubscribersSql);
             final ResultSet resultSet = getSubscribersPS.executeQuery())
        {
            Collection<RemoteSubscriber> results = new LinkedList<>();
            while (resultSet.next())
            {
                int paramIndex = 0;
                Integer id = resultSet.getInt(++paramIndex);
                String restUrl = resultSet.getString(++paramIndex);
                Integer timeout = resultSet.getInt(++paramIndex);
                String name = resultSet.getString(++paramIndex);
                Boolean autoRegiser = resultSet.getBoolean(++paramIndex);
                String topic = resultSet.getString(++paramIndex);
                String filterString = resultSet.getString(++paramIndex);

                results.add(new RemoteSubscriber(id, name, restUrl, filterString, MaxTopic.valueOf(topic), timeout, autoRegiser));
            }

            return results;
        }
        catch (SQLException | IllegalArgumentException e)
        {
            //log.error("Failure attempting to get Customers for fixing pay type", e);
        }

        return Collections.emptyList();
    }

    @Transactional
    public boolean delete(RemoteSubscriber saved)
    {
        String getSubscribersSql = "delete from RemoteSubscriber where id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getSubscribersPS = connection.prepareStatement(getSubscribersSql))
        {
            getSubscribersPS.setInt(1, saved.getId());

            final int i = getSubscribersPS.executeUpdate();
            return i == 1;
        }
        catch (SQLException | IllegalArgumentException e)
        {
            log.error(e);
            e.printStackTrace();
        }

        return false;
    }
}
