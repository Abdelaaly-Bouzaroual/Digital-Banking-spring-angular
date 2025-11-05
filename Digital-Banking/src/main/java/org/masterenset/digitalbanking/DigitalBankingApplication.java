package org.masterenset.digitalbanking;

import org.masterenset.digitalbanking.entities.*;
import org.masterenset.digitalbanking.enums.AccountStatus;
import org.masterenset.digitalbanking.enums.OperationType;
import org.masterenset.digitalbanking.repositories.AccountOperationRepository;
import org.masterenset.digitalbanking.repositories.BankAccountRepository;
import org.masterenset.digitalbanking.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Currency;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository
	) {
		return args -> {

			BankAccount bankAccount  = bankAccountRepository.findById("1d3d6a35-62bb-4d28-831f-9ad52b374479").orElse(null);
			if (bankAccount !=null){
			System.out.println("*******************************************");
			System.out.println(bankAccount.getId());
			System.out.println(bankAccount.getBalance());
			System.out.println(bankAccount.getStatus());
			System.out.println(bankAccount.getCreatedAt());
			System.out.println(bankAccount.getCustomer().getName());
			System.out.println(" *********** Afficher le nome de la classe du compte "+  bankAccount.getClass().getSimpleName());

			if(bankAccount instanceof CurrentAccount){
				double overDraft = ((CurrentAccount)bankAccount).getOverDraft();
				System.out.println("************* Overdraft: " + overDraft);
			} else if(bankAccount instanceof SavingAccount){
				double interestRate = ((SavingAccount)bankAccount).getInterestRate();
				System.out.println("************* interestRate: " + interestRate);
			}

			bankAccount.getAccountOperations().forEach(accountOperation -> {
				System.out.println( "===============================");
				System.out.println(accountOperation.getOperationDate());
				System.out.println(accountOperation.getAmount());
				System.out.println(accountOperation.getType());
			});
			}
		};
	}








//	@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository
									) {


	return args -> {
		Stream.of("Hassan", "yassine", "Aicha").forEach(name -> {
			Customer customer = new Customer();
			customer.setName(name);
			customer.setEmail(name + "@gmail.com");
			customerRepository.save(customer);
		});
		customerRepository.findAll().forEach(cus ->{
			CurrentAccount currentAccount = new CurrentAccount();
			currentAccount.setId(UUID.randomUUID().toString());
			currentAccount.setBalance(Math.random() * 90000);
			currentAccount.setCreatedAt(new Date());
			currentAccount.setStatus(AccountStatus.CREATED);
			currentAccount.setCustomer(cus);
			currentAccount.setOverDraft(9000);
			bankAccountRepository.save(currentAccount);

			SavingAccount savingAccount = new SavingAccount();
			savingAccount.setId(UUID.randomUUID().toString());
			savingAccount.setBalance(Math.random() * 90000);
			savingAccount.setCreatedAt(new Date());
			savingAccount.setStatus(AccountStatus.CREATED);
			savingAccount.setCustomer(cus);
			savingAccount.setInterestRate(5.5);
			bankAccountRepository.save(savingAccount);
		});
		bankAccountRepository.findAll().forEach(acc ->{
			for (int i = 0; i < 10; i++) {
				AccountOperation accountOperation = new AccountOperation();
				accountOperation.setOperationDate(new Date());
				accountOperation.setAmount(Math.random() * 12000);
				accountOperation.setType(Math.random()>0.5 ? OperationType.DEBIT : OperationType.CREDIT);
				accountOperation.setBankAccount(acc);
				accountOperationRepository.save(accountOperation);
			}

		});
	};
	}


}
