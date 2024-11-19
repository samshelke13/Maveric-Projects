package com.maveric.bank.repository;

import com.maveric.bank.entity.LoanRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRegistrationRepository extends JpaRepository<LoanRegistration, Long> {
}
