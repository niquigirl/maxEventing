package com.max.exigo;

import com.max.exigo.repositories.CustomerRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Verify functionality/plumbing of CustomerRepository
 */
public class CustomerRepositoryTest
{
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void testSomething()
    {
        Customer one = customerRepository.findOne(1);

        assertThat(one).isNotNull();

        System.out.println("Asserted could pull customer " + one.getLegalName());
    }
}
