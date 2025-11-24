package org.masterenset.digitalbanking.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.masterenset.digitalbanking.dtos.AccountHistoryDto;
import org.masterenset.digitalbanking.dtos.AccountOperationDto;
import org.masterenset.digitalbanking.dtos.CustomerDto;
import org.masterenset.digitalbanking.entities.AccountOperation;
import org.masterenset.digitalbanking.entities.Customer;
import org.masterenset.digitalbanking.exception.BankAccountNotFoundException;
import org.masterenset.digitalbanking.exception.CustomerNotFountException;
import org.masterenset.digitalbanking.repositories.CustomerRepository;
import org.masterenset.digitalbanking.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/customers")
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private final CustomerRepository customerRepository;
    private BankAccountService bankAccountService;;

    @GetMapping("/customers")
    public List<CustomerDto> customers(){
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/{id}")
    public CustomerDto getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFountException {
        return bankAccountService.getCustomer(customerId);
    }
    @PostMapping("/customers")
    public CustomerDto saveCustomer(@RequestBody CustomerDto customerDto)  {
        return bankAccountService.saveCustomer(customerDto);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDto updateCustomer(@RequestBody CustomerDto customerDto, @PathVariable Long customerId)  {
        customerDto.setId(customerId);
        return bankAccountService.updateCustomer(customerDto);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId)  {
        bankAccountService.deleteCustomer(customerId);
    }


    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDto> getHistory(@PathVariable(name = "id") String accoundId)   {
        return bankAccountService.accountHistory(accoundId);
    }


    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDto getAccountHistory
            (@PathVariable(name = "id") String accoundId,
             @RequestParam(name = "page" , defaultValue = "0") int page,
             @RequestParam(name = "page" , defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accoundId, page, size);

    }

}
