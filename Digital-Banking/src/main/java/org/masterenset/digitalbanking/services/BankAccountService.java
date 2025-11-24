package org.masterenset.digitalbanking.services;


import org.masterenset.digitalbanking.dtos.*;
import org.masterenset.digitalbanking.exception.BalanceNotSufficentException;
import org.masterenset.digitalbanking.exception.BankAccountNotFoundException;
import org.masterenset.digitalbanking.exception.CustomerNotFountException;

import java.util.List;

public interface BankAccountService {

    CustomerDto saveCustomer(CustomerDto customerDto);
//    BankAccount saveBankAccount(double initialBalance, String type, Long customerId) throws CustomerNotFountException;
    CurrentBankAccountDto saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException;
    SanvingBankAccountDto saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException;

    List<CustomerDto> listCustomers();
    BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId, double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSourse,String accountIdDestination,  double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;


    List<BankAccountDto>bankAccountList();

    CustomerDto getCustomer(Long customerId) throws CustomerNotFountException;

    CustomerDto updateCustomer(CustomerDto customerdto);

    void deleteCustomer(Long customerId);

    List<AccountOperationDto> accountHistory(String accountId);

    AccountHistoryDto getAccountHistory(String accoundId, int page, int size) throws BankAccountNotFoundException;
}
