package org.masterenset.digitalbanking.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.masterenset.digitalbanking.entities.BankAccount;
import org.masterenset.digitalbanking.entities.CurrentAccount;
import org.masterenset.digitalbanking.entities.Customer;
import org.masterenset.digitalbanking.entities.SavingAccount;
import org.masterenset.digitalbanking.enums.AccountStatus;
import org.masterenset.digitalbanking.exception.CustomerNotFountException;
import org.masterenset.digitalbanking.repositories.AccountOperationRepository;
import org.masterenset.digitalbanking.repositories.BankAccountRepository;
import org.masterenset.digitalbanking.repositories.CustomerRepository;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
//@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository, AccountOperationRepository accountOperationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.accountOperationRepository = accountOperationRepository;
    }

//    Logger log = LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("saving new customer");
        return customerRepository.save(customer);
    }

    @Override
    public BankAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFountException("Customer not found");
        }
        CurrentAccount currentAccount= new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCustomer(customer);
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount saveCurrentAccount = bankAccountRepository.save(currentAccount);
        return saveCurrentAccount;
    }

    @Override
    public BankAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFountException("Customer not found");
        }
        SavingAccount savingAccount= new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCustomer(customer);
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        SavingAccount saveBankAccount = bankAccountRepository.save(savingAccount);
        return saveBankAccount;
    }


    @Override
    public List<Customer> listCustomers() {
        return List.of();
    }

    @Override
    public BankAccount getBankAccount(Long accountId) {
        return null;
    }

    @Override
    public void debit(String accountId, double amount, String description) {

    }

    @Override
    public void credit(String accountId, double amount, String description) {

    }

    @Override
    public void transfer(String accountIdSourse, String accountIdDestination, double amount) {

    }
}
