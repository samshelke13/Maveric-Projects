package com.maveric.bank.dto;

import java.time.LocalDate;

import com.maveric.bank.entity.Loan;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {

    private Long loanId;

    @NotNull(message = "Issued amount is required")
    @DecimalMin(value = "1.0", message = "Issued amount must be greater than zero")
    private Double issuedAmount;

    private Double remainingAmount; // No validation as it might be calculated later

    @NotNull(message = "Account ID is required")
    @Pattern(regexp = "\\d{10}", message = "Account ID must be a 10-digit number")
    private String accountId;

    @NotNull(message = "Branch ID is required")
    @Pattern(regexp = "BR\\d{4}", message = "Branch ID must start with 'BR' followed by 4 digits")
    private String branchId;

    @NotNull(message = "Sanction date is required")
    @PastOrPresent(message = "Sanction date must be in the past or today")
    private LocalDate sanctionDate;

    @NotNull(message = "Sanction period is required")
    @Min(value = 1, message = "Sanction period must be at least 1 month")
    private Integer sanctionPeriod;

    @NotNull(message = "Interest rate is required")
    @Min(value = 0, message = "Interest rate must be positive")
    private Double interestRate;

    private Double monthlyInterestAmount; // Optional, might be calculated

    private Double emi; // Optional, might be calculated

    // Convert LoanDTO to Loan entity
    public Loan toEntity() {
        Loan loan = new Loan();
        loan.setIssuedAmount(this.issuedAmount);
        loan.setSanctionDate(this.sanctionDate);
        loan.setSanctionPeriod(this.sanctionPeriod);
        loan.setInterestRate(this.interestRate);
        loan.setAccountId(this.accountId);
        loan.setBranchId(this.branchId);
        loan.setRemainingAmount(this.remainingAmount); // If you calculate this value, otherwise remove it
        return loan;
    }
}