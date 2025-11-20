package org.masterenset.digitalbanking.services;


import org.masterenset.digitalbanking.entities.BankAccount;
import org.masterenset.digitalbanking.entities.CurrentAccount;
import org.masterenset.digitalbanking.entities.Customer;
import org.masterenset.digitalbanking.entities.SavingAccount;
import org.masterenset.digitalbanking.exception.CustomerNotFountException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BankAccountService {

    Customer saveCustomer(Customer customer);
//    BankAccount saveBankAccount(double initialBalance, String type, Long customerId) throws CustomerNotFountException;
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException;

    List<Customer> listCustomers();
    BankAccount getBankAccount(Long accountId);
    void debit(String accountId, double amount,String description);
    void credit(String accountId, double amount,String description);
    void transfer(String accountIdSourse,String accountIdDestination,  double amount);


}
