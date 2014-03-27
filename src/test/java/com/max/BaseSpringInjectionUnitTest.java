package com.max;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * The base unit test class to extend any unit tests that require/validate Spring injection/components
 * Created by neastman on 12/11/13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/spring/max-applicationContext.xml",
        "classpath:/spring/max-mvc.xml",
        "classpath:/spring/max-jpa.xml"
/*
        "classpath:/spring/max-exigo.xml",
        "classpath:/spring/max-services.xml",
        "classpath:/spring/max-jms.xml",
*/
})
public abstract class BaseSpringInjectionUnitTest
{
}

