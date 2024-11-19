package com.maveric.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maveric.bank.dto.CustomerDTO;
import com.maveric.bank.entity.Customer;
import com.maveric.bank.service.ICustomerService;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private ICustomerService customerService;

	@PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerDTO customerDTO) {
        // Get the success message from the service
        String successMessage = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(successMessage, HttpStatus.CREATED); // Return message with status 201
    }

	@GetMapping
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
		return customerService.getCustomerById(customerId)
				.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/{customerId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
		customerService.deleteCustomer(customerId); // Delete the customer
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 status
	}
}
