package com.maveric.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.bank.dto.LoanDTO;
import com.maveric.bank.service.LoanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // POST - Create a new loan
    @PostMapping
    public ResponseEntity<?> createLoan(@Valid @RequestBody LoanDTO loanDTO, BindingResult result) {
        if (result.hasErrors()) {
            // Collect validation errors
            StringBuilder errors = new StringBuilder();
            result.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
            return ResponseEntity.badRequest().body(errors.toString());
        }

        LoanDTO createdLoan = loanService.createLoan(loanDTO);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    // GET - Retrieve a loan by loanId
    @GetMapping("/{loanId}")
    public ResponseEntity<LoanDTO> getLoan(@PathVariable Long loanId) {
        LoanDTO loanDTO = loanService.getLoanById(loanId);
        return ResponseEntity.ok(loanDTO);
    }

    // GET - Retrieve all loans
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // PUT - Update an existing loan
    @PutMapping("/{loanId}")
    public ResponseEntity<?> updateLoan(@PathVariable Long loanId, @Valid @RequestBody LoanDTO loanDTO, BindingResult result) {
        if (result.hasErrors()) {
            // Collect validation errors
            StringBuilder errors = new StringBuilder();
            result.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
            return ResponseEntity.badRequest().body(errors.toString());
        }

        LoanDTO updatedLoan = loanService.updateLoan(loanId, loanDTO);
        return ResponseEntity.ok(updatedLoan);
    }

    // DELETE - Delete a loan by loanId
    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long loanId) {
        loanService.deleteLoan(loanId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}