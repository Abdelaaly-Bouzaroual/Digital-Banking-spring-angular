package org.masterenset.digitalbanking.dtos;

import jakarta.persistence.*;
import lombok.Data;
import org.masterenset.digitalbanking.entities.Customer;
import org.masterenset.digitalbanking.enums.AccountStatus;

import java.util.Date;
import java.util.List;


@Data
public class CurrentBankAccountDto extends BankAccountDto {

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    @ManyToOne
    private CustomerDto customerDto;
    private double overdraft;

}
