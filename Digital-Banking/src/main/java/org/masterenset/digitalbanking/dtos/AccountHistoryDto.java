package org.masterenset.digitalbanking.dtos;

import lombok.Data;
import org.masterenset.digitalbanking.entities.AccountOperation;

import java.util.List;

@Data
public class AccountHistoryDto {
    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    List<AccountOperationDto> accountOperationDto;

}
