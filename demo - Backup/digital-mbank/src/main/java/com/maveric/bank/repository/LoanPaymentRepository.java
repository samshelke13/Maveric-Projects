package com.maveric.bank.repository;

import com.maveric.bank.entity.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Long> {
    List<LoanPayment> findByLoan_LoanId(Long loanId);
}
