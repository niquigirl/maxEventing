package com.max.exigo;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Special data accessor methods around Customers. In this way we can leverage the abilities of straight JDBC where needed
 * I'm using this rather than Repositories when join tables are needed.  I don't yet know how to avoid Hibernate's running
 * a separate query for each record when joins are needed
 */
public class CustomerDao
{
    DataSource exigoDataSource;

    Logger log = Logger.getLogger(CustomerDao.class);

    public Integer getEnrollerId(Integer associateId)
    {
        String sql = "select EnrollerID from Customers where CustomerID = ?";
        return getInteger(associateId, sql);
    }

    public Integer getSponsorId(Integer associateId)
    {
        String sql = "select SponsorID from Customers where CustomerID = ?";
        return getInteger(associateId, sql);
    }

    public Integer getUplineBronzeId(Integer associateId)
    {
        String sql = "select max(upline.customerID) from BinaryUpline upline " +
                "join Customers c on c.CustomerID = upline.CustomerID and c.CustomerId <> upline.UplineCustomerID " +
                "join Ranks r on r.RankID = c.RankID " +
                "where upline.UplineCustomerID = ? and r.RankDescription like '%Bronze%'";
        return getInteger(associateId, sql);
    }

    public Integer getUplineSilverId(Integer associateId)
    {
        String sql = "select max(upline.customerID) from BinaryUpline upline " +
                "join Customers c on c.CustomerID = upline.CustomerID and c.CustomerId <> upline.UplineCustomerID " +
                "join Ranks r on r.RankID = c.RankID " +
                "where upline.UplineCustomerID = ? and r.RankDescription like '%Silver%'";
        return getInteger(associateId, sql);
    }

    public Integer getUplineGoldId(Integer associateId)
    {
        String sql = "select max(upline.customerID) from BinaryUpline upline " +
                "join Customers c on c.CustomerID = upline.CustomerID and c.CustomerId <> upline.UplineCustomerID " +
                "join Ranks r on r.RankID = c.RankID " +
                "where upline.UplineCustomerID = ? and r.RankDescription like '%Gold%'";
        return getInteger(associateId, sql);
    }

    /**
     * Utility that takes a SQL statement and a single, Integer, param and retrieves a single, Integer, result
     *
     * @param statementParamValue {@code Integer}
     * @param sql {@code String}
     * @return {@code Integer}, or null if no value could be found
     */
    @Transactional
    private Integer getInteger(Integer statementParamValue, String sql)
    {
        Integer value = null;

        try (Connection connection = exigoDataSource.getConnection();
             PreparedStatement getRelatedUserStatement = connection.prepareStatement(sql))
        {
            getRelatedUserStatement.setInt(1, statementParamValue);
            final ResultSet resultSet = getRelatedUserStatement.executeQuery();

            if (resultSet.next())
            {
                value = resultSet.getInt(1);
            }

            getRelatedUserStatement.close();
        }
        catch (SQLException | IllegalArgumentException e)
        {
            log.error("Failure attempting to run the following sql statement " + sql + " WITH PARAM [" + statementParamValue + "]", e);
        }

        return value;
    }

    public DataSource getExigoDataSource()
    {
        return exigoDataSource;
    }

    public void setExigoDataSource(DataSource exigoDataSource)
    {
        this.exigoDataSource = exigoDataSource;
    }
}
