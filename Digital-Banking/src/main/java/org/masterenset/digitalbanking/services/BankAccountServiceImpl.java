package org.masterenset.digitalbanking.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.masterenset.digitalbanking.dtos.CustomerDto;
import org.masterenset.digitalbanking.entities.*;
import org.masterenset.digitalbanking.enums.AccountStatus;
import org.masterenset.digitalbanking.enums.OperationType;
import org.masterenset.digitalbanking.exception.BalanceNotSufficentException;
import org.masterenset.digitalbanking.exception.BankAccountNotFoundException;
import org.masterenset.digitalbanking.exception.CustomerNotFountException;
import org.masterenset.digitalbanking.mappers.BankAccountMapperImpl;
import org.masterenset.digitalbanking.repositories.AccountOperationRepository;
import org.masterenset.digitalbanking.repositories.BankAccountRepository;
import org.masterenset.digitalbanking.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;



//    Logger log = LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public CustomerDto saveCustomer(CustomerDto customerdto) {
        log.info("saving new customer");
        Customer customer = dtoMapper.fromCustomerDto(customerdto);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException {
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
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException {
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
    public List<CustomerDto> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).toList();

        /*
        List<CustomerDto> customerDtos = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDto customerDto = dtoMapper.fromCustomer(customer);
            customerDtos.add(customerDto);
        }
        */

        return customerDTOS;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("BankAccound not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount = getBankAccount(accountId);
        if(bankAccount.getBalance() < amount)
            throw new BalanceNotSufficentException("Balance not sufficent");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSourse, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSourse, amount, "Transfert to "+ accountIdDestination);
        credit(accountIdDestination, amount, "Transfert from "+accountIdSourse );
    }

    @Override
    public List<BankAccount>bankAccountList(){
        return bankAccountRepository.findAll();
    }

    @Override
    public CustomerDto getCustomer(Long customerId) throws CustomerNotFountException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFountException("customer Not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerdto) {
        log.info("saving new customer");
        Customer customer = dtoMapper.fromCustomerDto(customerdto);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }



}
