package org.masterenset.digitalbanking.repositories;

import org.masterenset.digitalbanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
