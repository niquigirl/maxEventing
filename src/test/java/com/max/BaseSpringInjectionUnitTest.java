package com.max;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.sql.DataSource;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


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
        "classpath:/spring/max-jpa.xml",
        "classpath:/spring/max-services.xml"
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@WebAppConfiguration
public abstract class BaseSpringInjectionUnitTest extends AbstractTransactionalJUnit4SpringContextTests
{
/*
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
//    "text/plain;charset=ISO-8859-1"
    public static final MediaType APPLICATION_TEXT= new MediaType(MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName("iso-8859-1"));
*/

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;


    @Before
    public void setup()
    {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Resource(name = "dataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}

