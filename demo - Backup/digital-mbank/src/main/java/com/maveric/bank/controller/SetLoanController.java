package com.maveric.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maveric.bank.dto.SetLoanDTO;
import com.maveric.bank.service.SetLoanService;

import java.util.List;

@RestController
@RequestMapping("/setloans")
public class SetLoanController {

    @Autowired
    private SetLoanService setLoanService;

    @GetMapping
    public ResponseEntity<List<SetLoanDTO>> getAllLoans() {
        return ResponseEntity.ok(setLoanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SetLoanDTO> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(setLoanService.getLoanById(id));
    }

    @PostMapping
    public ResponseEntity<SetLoanDTO> createLoan(@RequestBody SetLoanDTO setLoanDTO) {
        return ResponseEntity.ok(setLoanService.createLoan(setLoanDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SetLoanDTO> updateLoan(@PathVariable Long id, @RequestBody SetLoanDTO setLoanDTO) {
        return ResponseEntity.ok(setLoanService.updateLoan(id, setLoanDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        setLoanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}
