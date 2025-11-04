package org.masterenset.digitalbanking.repositories;

import org.masterenset.digitalbanking.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
