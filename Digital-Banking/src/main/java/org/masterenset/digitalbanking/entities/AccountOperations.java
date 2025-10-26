package org.masterenset.digitalbanking.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.masterenset.digitalbanking.enums.OperationType;

import java.util.Date;
@Entity
@Data @AllArgsConstructor
@NoArgsConstructor
public class AccountOperations {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private BankAccount bankAccount;
}
