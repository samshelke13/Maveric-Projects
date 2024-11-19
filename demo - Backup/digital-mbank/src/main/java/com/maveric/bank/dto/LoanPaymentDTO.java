package com.maveric.bank.dto;

import java.time.LocalDate;

public class LoanPaymentDTO {
    
    private Long paymentId;
    private Long loanId;  // Corresponding Loan ID
    private Double paidAmount;
    private LocalDate paymentDate;
    private Double remainingAmountAfterPayment;
    private Double issuedAmount;
    private Double emi;
    private Double interestRate;
	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	public Double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}
	public LocalDate getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Double getRemainingAmountAfterPayment() {
		return remainingAmountAfterPayment;
	}
	public void setRemainingAmountAfterPayment(Double remainingAmountAfterPayment) {
		this.remainingAmountAfterPayment = remainingAmountAfterPayment;
	}
	public Double getIssuedAmount() {
		return issuedAmount;
	}
	public void setIssuedAmount(Double issuedAmount) {
		this.issuedAmount = issuedAmount;
	}
	public Double getEmi() {
		return emi;
	}
	public void setEmi(Double emi) {
		this.emi = emi;
	}
	public Double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

    
}
