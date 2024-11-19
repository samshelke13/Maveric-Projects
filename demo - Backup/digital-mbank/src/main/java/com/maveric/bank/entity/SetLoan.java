package com.maveric.bank.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
@Entity
@Table(name = "set_loans")
public class SetLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SetLoanEnum title;

    @DecimalMax(value = "1000000000", message = "Loan amount cannot exceed 10 crore")
    private BigDecimal loanAmount;

    @Max(value = 360, message = "Loan tenure cannot exceed 30 years (360 months)")
    private int loanTenure;

    private BigDecimal interestRate;
    private BigDecimal calculatedEMI;
    private BigDecimal totalInterestAmount;
    private BigDecimal totalAmountPayable;

    public void calculateLoanDetails() {
        if (loanAmount == null || loanTenure <= 0 || interestRate == null) {
            throw new IllegalArgumentException("Loan amount, tenure, and interest rate must be valid and greater than zero");
        }

        BigDecimal monthlyInterestRate = interestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                                                     .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            this.calculatedEMI = loanAmount.divide(BigDecimal.valueOf(loanTenure), 2, RoundingMode.HALF_UP);
            this.totalInterestAmount = BigDecimal.ZERO;
            this.totalAmountPayable = loanAmount;
            return;
        }

        BigDecimal onePlusRatePowerN = BigDecimal.ONE.add(monthlyInterestRate).pow(loanTenure);
        BigDecimal numerator = loanAmount.multiply(monthlyInterestRate).multiply(onePlusRatePowerN);
        BigDecimal denominator = onePlusRatePowerN.subtract(BigDecimal.ONE);

        this.calculatedEMI = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        this.totalAmountPayable = this.calculatedEMI.multiply(BigDecimal.valueOf(loanTenure)).setScale(2, RoundingMode.HALF_UP);
        this.totalInterestAmount = this.totalAmountPayable.subtract(loanAmount).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal adjustInterestRateByLoanType() {
        switch (this.title) {
            case HOME: return BigDecimal.valueOf(5.0);
            case PERSONAL: return BigDecimal.valueOf(10.0);
            case CAR: return BigDecimal.valueOf(7.5);
            case GOLD: return BigDecimal.valueOf(8.0);
            case EDUCATION: return BigDecimal.valueOf(4.0);
            case AGRICULTURAL: return BigDecimal.valueOf(3.5);
            case BUSINESS: return BigDecimal.valueOf(9.0);
            default: return BigDecimal.ZERO;
        }
    }

    public String getTitleDescription() {
        switch (this.title) {
            case HOME: return "Home Loan";
            case PERSONAL: return "Personal Loan";
            case CAR: return "Car Loan";
            case GOLD: return "Gold Loan";
            case EDUCATION: return "Education Loan";
            case AGRICULTURAL: return "Agricultural Loan";
            case BUSINESS: return "Business Loan";
            default: return "Unknown Loan Type";
        }
    }
}
