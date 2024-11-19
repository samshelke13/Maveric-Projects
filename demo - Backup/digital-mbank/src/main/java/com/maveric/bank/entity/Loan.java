package com.maveric.bank.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @Column(nullable = false)
    private Double issuedAmount;

    private Double remainingAmount;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String branchId;

    @Column(nullable = false)
    private LocalDate sanctionDate;

    @Column(nullable = false)
    private Integer sanctionPeriod;

    @Column(nullable = false)
    private Double interestRate;

    private Double monthlyInterestAmount;

    private Double emi;
}
