package org.masterenset.digitalbanking.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.masterenset.digitalbanking.dtos.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public CurrentBankAccountDto saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException {
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
        return dtoMapper.fromCurrentAccount(saveCurrentAccount);
    }

    @Override
    public SanvingBankAccountDto saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException {
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
        return dtoMapper.fromSavingBankAccount(saveBankAccount);
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
    public BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = getAccount(accountId);
        if (bankAccount instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentAccount(currentAccount);
        }else {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount = getAccount(accountId);
        if(bankAccount.getBalance() < amount)
            throw new BalanceNotSufficentException("Balance not sufficent");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        accountOperation.setBankAccount(bankAccount); 
        accountOperationRepository.save(accountOperation);
        bankAccountRepository.save(bankAccount);
        
    }

    private BankAccount getAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("BankAccound not found"));
        return bankAccount;
    }

    @Override
    public void transfer(String accountIdSourse, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSourse, amount, "Transfert to "+ accountIdDestination);
        credit(accountIdDestination, amount, "Transfert from "+accountIdSourse );
    }

    @Override
    public List<BankAccountDto>bankAccountList(){
         List<BankAccount> bankAccounts =  bankAccountRepository.findAll();
        List<BankAccountDto> bankAccountDtos = bankAccounts.stream().map(bankAccount ->{
            if (bankAccount instanceof CurrentAccount) {
                return dtoMapper.fromCurrentAccount((CurrentAccount) bankAccount);
            }else {
                return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
            }

        }).toList();
        return bankAccountDtos;
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

    @Override
    public List<AccountOperationDto> accountHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op ->dtoMapper.fromOperationAccount(op)).toList();

    }

    @Override
    public AccountHistoryDto getAccountHistory(String accoundId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accoundId).orElse(null);
        if(bankAccount == null) throw new BankAccountNotFoundException("account not found ");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accoundId, PageRequest.of(page, size));
        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();
        List<AccountOperationDto> accountOperationsDtos = accountOperations.getContent().stream().map(op -> dtoMapper.fromOperationAccount(op)).toList();
        accountHistoryDto.setAccountOperationDto(accountOperationsDtos);
        accountHistoryDto.setAccountId(bankAccount.getId());
        accountHistoryDto.setBalance(bankAccount.getBalance());
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setPageSize(size);
        accountHistoryDto.setTotalPages(accountOperations.getTotalPages());
        return  accountHistoryDto;
    }


}
