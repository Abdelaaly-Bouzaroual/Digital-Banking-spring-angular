package org.masterenset.digitalbanking.mappers;

import org.masterenset.digitalbanking.dtos.AccountOperationDto;
import org.masterenset.digitalbanking.dtos.CurrentBankAccountDto;
import org.masterenset.digitalbanking.dtos.CustomerDto;
import org.masterenset.digitalbanking.dtos.SanvingBankAccountDto;
import org.masterenset.digitalbanking.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

// MapStruct
//ModelMapper
@Service
public class BankAccountMapperImpl {
    public CustomerDto fromCustomer(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }


    public Customer fromCustomerDto(CustomerDto customerDto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        return customer;
    }

// ------------------------ saving accout mapper ------------------------ \\


    public SanvingBankAccountDto fromSavingBankAccount(SavingAccount SavingAccount) {
        SanvingBankAccountDto sanvingBankAccountDto = new SanvingBankAccountDto();
        BeanUtils.copyProperties(SavingAccount, sanvingBankAccountDto);
         sanvingBankAccountDto.setCustomerDto(fromCustomer(SavingAccount.getCustomer()));
        sanvingBankAccountDto.setType(SavingAccount.getClass().getSimpleName());
        return sanvingBankAccountDto;
    }

    public SavingAccount fromSavingBankAccountDto(SanvingBankAccountDto sanvingBankAccountDto) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(sanvingBankAccountDto, savingAccount);
        savingAccount.setCustomer(fromCustomerDto(sanvingBankAccountDto.getCustomerDto()));
        return savingAccount;
    }

// ------------------------ current accout mapper ------------------------ \\
    public CurrentBankAccountDto fromCurrentAccount(CurrentAccount currentAccount) {
        CurrentBankAccountDto currentBankAccountDto = new CurrentBankAccountDto();
        BeanUtils.copyProperties(currentAccount, currentBankAccountDto);
        currentBankAccountDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDto.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDto;
    }

    public CurrentAccount fromCurrentAccountDto(CurrentBankAccountDto curentBankAccountDto) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(curentBankAccountDto, currentAccount);
        currentAccount.setCustomer(fromCustomerDto(curentBankAccountDto.getCustomerDto()));


        return currentAccount;
    }

    // ------------------------ operation accout mapper ------------------------ \\
    public AccountOperationDto fromOperationAccount(AccountOperation accountOperation) {
        AccountOperationDto accountOperationDto = new AccountOperationDto();
        BeanUtils.copyProperties(accountOperation, accountOperationDto);
        return accountOperationDto;
    }

    public AccountOperation fromOperationAccountDto(AccountOperationDto accountOperationDto) {
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDto, accountOperation);
        return accountOperation;
    }


}
