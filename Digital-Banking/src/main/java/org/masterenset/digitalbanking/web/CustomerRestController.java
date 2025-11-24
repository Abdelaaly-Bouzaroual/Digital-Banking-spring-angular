package org.masterenset.digitalbanking.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.masterenset.digitalbanking.dtos.CustomerDto;
import org.masterenset.digitalbanking.entities.Customer;
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
    public CustomerDto updateCustomer(@RequestBody CustomerDto customerDto, @PathVariable Long customerId) throws CustomerNotFountException {
        customerDto.setId(customerId);
        return bankAccountService.updateCustomer(customerDto);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFountException {
        bankAccountService.deleteCustomer(customerId);
    }

}
