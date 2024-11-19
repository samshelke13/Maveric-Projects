package com.maveric.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.maveric.bank.dto.CustomerDTO;
import com.maveric.bank.entity.Account;
import com.maveric.bank.entity.Customer;
import com.maveric.bank.repository.CustomerRepository;
import com.maveric.bank.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired 
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public String createCustomer(CustomerDTO customerDTO) {
        validateCustomerDTO(customerDTO); 
        Customer customer = new Customer();
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setDob(customerDTO.getDob());
        customer.setMobileNumber(customerDTO.getMobileNo());
        customer.setCustomerId(generateUniqueCustomerId());

        Account account = accountService.createAccount(customerDTO.getAccount());
        customer.setAccount(account);

       
        
        customerRepository.save(customer);

        return "Customer created successfully. Account ID: " + account.getAccountId()+", Customer ID: "+customer.getCustomerId();
    }
    
	private Long generateUniqueCustomerId() {
		Long maxId = customerRepository.findMaxBankerId();
		if (maxId == null || maxId < 00001) {
			return 000001L;
		}
		return maxId + 1;
	}

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Customer not found with ID: " + customerId);
        }
        return customerRepository.findById(customerId);
    }

    @Override
    public String deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Customer not found with ID: " + customerId);
        }

      //  transactionRepository.deleteByCustomerId(customerId); 

        customerRepository.deleteById(customerId);
        return "Customer with ID " + customerId + " deleted successfully.";
    }


    // Validation method for CustomerDTO
    private void validateCustomerDTO(CustomerDTO customerDTO) {
        // Validate customer name
        if (customerDTO.getCustomerName() == null || customerDTO.getCustomerName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer name cannot be null or empty");
        }

        // Validate date of birth (ensure it's not null)
        if (customerDTO.getDob() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of Birth cannot be null");
        }

        // Validate mobile number (must be exactly 10 digits)
        if (customerDTO.getMobileNo() == null || customerDTO.getMobileNo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mobile number cannot be null or empty");
        }
        if (!customerDTO.getMobileNo().matches("\\d{10}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mobile number must be exactly 10 digits");
        }

        // Validate account details
        if (customerDTO.getAccount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account details must be provided");
        }
    }
}