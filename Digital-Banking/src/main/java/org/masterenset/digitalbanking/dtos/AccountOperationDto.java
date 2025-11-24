package org.masterenset.digitalbanking.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.masterenset.digitalbanking.entities.BankAccount;
import org.masterenset.digitalbanking.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDto {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
