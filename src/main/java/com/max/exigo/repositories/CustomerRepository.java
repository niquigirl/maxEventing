package com.max.exigo.repositories;

import com.max.exigo.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing/manipulating {@code Customer}s
 * Note: when a join table is needed, I can't avoid Hibernate's running a separate statement per record so I'm using CustomerDao for those cases
 * Created by neastman on 3/2/14.
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer>
{
}
