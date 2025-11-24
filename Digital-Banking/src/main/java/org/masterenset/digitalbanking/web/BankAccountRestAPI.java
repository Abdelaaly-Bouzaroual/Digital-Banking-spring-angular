package org.masterenset.digitalbanking.web;

import lombok.AllArgsConstructor;
import org.masterenset.digitalbanking.dtos.BankAccountDto;
import org.masterenset.digitalbanking.exception.BankAccountNotFoundException;
import org.masterenset.digitalbanking.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDto getBankAccountById(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }


    @GetMapping("/accounts")
    public List<BankAccountDto> listAccounts() throws BankAccountNotFoundException {
        return bankAccountService.bankAccountList();
    }
}
