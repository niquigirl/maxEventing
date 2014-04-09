package com.max.exigo;

import com.max.BaseExigoSpringInjectionUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Verify CustomerDao stuff
 */
public class CustomerDaoTest extends BaseExigoSpringInjectionUnitTest
{
    @Autowired
    CustomerDao customerDao;

    @Test
    public void testGetRelatedAssociate_Enroller() throws Exception
    {
        Integer testerId = 500035;
        Integer expectedResult = 2;

        Integer relatedAssociateId = customerDao.getEnrollerId(testerId);

        assertThat(relatedAssociateId).isEqualTo(expectedResult);

        System.out.println("Asserted getRelatedAssociate returns expected for Enroller");
    }

    @Test
    public void testGetRelatedAssociate_Sponsor() throws Exception
    {
        Integer testerId = 500035;
        Integer expectedResult = 136998;

        Integer relatedAssociateId = customerDao.getSponsorId(testerId);

        assertThat(relatedAssociateId).isEqualTo(expectedResult);

        System.out.println("Asserted getRelatedAssociate returns expected for Sponsor");
    }

    @Test
    public void testGetRelatedAssociate_Bronze() throws Exception
    {
        Integer testerId = 511399;
        Integer expectedResult = 357157;

        Integer relatedAssociateId = customerDao.getUplineBronzeId(testerId);

        assertThat(relatedAssociateId).isEqualTo(expectedResult);

        System.out.println("Asserted getRelatedAssociate returns expected for Upline Bronze");
    }

    @Test
    public void testGetRelatedAssociate_Gold() throws Exception
    {
        Integer testerId = 511399;
        Integer expectedResult = 342297;

        Integer relatedAssociateId = customerDao.getUplineGoldId(testerId);

        assertThat(relatedAssociateId).isEqualTo(expectedResult);

        System.out.println("Asserted getRelatedAssociate returns expected for Gold");
    }
}
