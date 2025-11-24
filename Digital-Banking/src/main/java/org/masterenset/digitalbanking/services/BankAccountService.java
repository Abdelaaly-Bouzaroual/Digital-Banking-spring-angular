package org.masterenset.digitalbanking.services;


import org.masterenset.digitalbanking.dtos.CustomerDto;
import org.masterenset.digitalbanking.entities.BankAccount;
import org.masterenset.digitalbanking.entities.CurrentAccount;
import org.masterenset.digitalbanking.entities.Customer;
import org.masterenset.digitalbanking.entities.SavingAccount;
import org.masterenset.digitalbanking.exception.BalanceNotSufficentException;
import org.masterenset.digitalbanking.exception.BankAccountNotFoundException;
import org.masterenset.digitalbanking.exception.CustomerNotFountException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BankAccountService {

    CustomerDto saveCustomer(CustomerDto customerDto);
//    BankAccount saveBankAccount(double initialBalance, String type, Long customerId) throws CustomerNotFountException;
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException;

    List<CustomerDto> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId, double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSourse,String accountIdDestination,  double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;


    List<BankAccount>bankAccountList();

    CustomerDto getCustomer(Long customerId) throws CustomerNotFountException;

    CustomerDto updateCustomer(CustomerDto customerdto);

    void deleteCustomer(Long customerId);
}
