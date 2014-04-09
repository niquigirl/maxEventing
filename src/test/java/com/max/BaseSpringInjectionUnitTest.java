package com.max;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * The base unit test class to extend any unit tests that require/validate Spring injection/components
 * Created by neastman on 12/11/13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
/*@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})*/

@ContextConfiguration(locations = {
        "classpath:/spring/max-applicationContext.xml",
        "classpath:/spring/max-mvc.xml",
        "classpath:/spring/max-services.xml",
        "classpath:/spring/max-jms.xml",
        "classpath:/spring/max-jms-subscribers.xml",
        "classpath:/spring/max-jpa.xml"
})
@TransactionConfiguration(transactionManager = "maxTransactionManager", defaultRollback = true)
@Transactional
public abstract class BaseSpringInjectionUnitTest extends AbstractTransactionalJUnit4SpringContextTests
{
    @Resource(name = "maxDataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}

