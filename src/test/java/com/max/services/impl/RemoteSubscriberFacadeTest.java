package com.max.services.impl;

import com.max.BaseMockUnitTest;
import org.junit.Ignore;
import org.junit.Test;

public class RemoteSubscriberFacadeTest extends BaseMockUnitTest
{

    @Test
    @Ignore
    public void testSendSomething()
    {
        RemoteSubscriberFacade facade = new RemoteSubscriberFacade();
//        facade.setServiceUrl("http://maxbussvcs.com/testMessaging/post");
        facade.setServiceUrl("https://maxbussvcs.com:8443/secure/1.0/en/us/onAutoOrderLocaleDIViolation");
        facade.setName("Stand alone");

        facade.onMessage("{\"object\":{\"customerStatus\":\"Active\",\"customerId\":529798,\"warehouseId\":22,\"warehouseDescription\":\"Ghana\",\"country\":\"GH\",\"currencyCode\":\"usd\",\"objectType\":\"CustomerLocaleReport\",\"index\":529798},\"actor\":{\"objectType\":\"System\",\"displayName\":\"MaxScheduledJob\",\"objectSubtype\":\"MaxBusinessServices\"},\"verb\":\"SomeTestVerb\",\"published\":null,\"language\":\"en\",\"generator\":null,\"provider\":null,\"location\":null}");
    }
}
