package com.maveric.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.bank.dto.LoanPaymentDTO;
import com.maveric.bank.service.LoanPaymentService;

@RestController
@RequestMapping("/loan-payments")
public class LoanPaymentController {

    @Autowired
    private LoanPaymentService loanPaymentService;

    // Create a new payment for a loan (POST)
    @PostMapping
    public LoanPaymentDTO createLoanPayment(@RequestBody LoanPaymentDTO loanPaymentDTO) {
        return loanPaymentService.createLoanPayment(loanPaymentDTO);
    }

    // Get all payments for a specific loan (GET by Loan ID)
    @GetMapping("/loan/{loanId}")
    public List<LoanPaymentDTO> getPaymentsByLoanId(@PathVariable Long loanId) {
        return loanPaymentService.getPaymentsByLoanId(loanId);
    }

    // Get a specific loan payment by paymentId (GET by Payment ID)
    @GetMapping("/{paymentId}")
    public LoanPaymentDTO getPaymentById(@PathVariable Long paymentId) {
        return loanPaymentService.getPaymentById(paymentId);
    }

    // Update an existing loan payment (PUT)
    @PutMapping("/{paymentId}")
    public LoanPaymentDTO updateLoanPayment(@PathVariable Long paymentId, @RequestBody LoanPaymentDTO loanPaymentDTO) {
        return loanPaymentService.updateLoanPayment(paymentId, loanPaymentDTO);
    }

    // Delete a loan payment by paymentId (DELETE)
    @DeleteMapping("/{paymentId}")
    public boolean deleteLoanPayment(@PathVariable Long paymentId) {
        return loanPaymentService.deleteLoanPayment(paymentId);
    }
}
