package com.maveric.bank.dto;

import java.math.BigDecimal;

import com.maveric.bank.entity.SetLoanEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetLoanDTO {
    private Long id;
    private SetLoanEnum title;
    private BigDecimal loanAmount;
    private int loanTenure;
    private BigDecimal interestRate;
    private BigDecimal calculatedEMI;
    private BigDecimal totalInterestAmount;
    private BigDecimal totalAmountPayable;
    private String titleDescription;
}
