package com.maveric.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maveric.bank.entity.SetLoan;

@Repository
public interface SetLoanRepository extends JpaRepository<SetLoan, Long> {
}
