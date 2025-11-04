package org.masterenset.digitalbanking.repositories;

import org.masterenset.digitalbanking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
