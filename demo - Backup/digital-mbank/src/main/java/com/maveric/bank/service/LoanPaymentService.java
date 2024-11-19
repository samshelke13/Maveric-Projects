package com.maveric.bank.service;

import com.maveric.bank.dto.LoanPaymentDTO;
import com.maveric.bank.entity.Loan;
import com.maveric.bank.entity.LoanPayment;
import com.maveric.bank.repository.LoanPaymentRepository;
import com.maveric.bank.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanPaymentService {

    @Autowired
    private LoanPaymentRepository loanPaymentRepository;

    @Autowired
    private LoanRepository loanRepository;

    // Create a new loan payment
    public LoanPaymentDTO createLoanPayment(LoanPaymentDTO loanPaymentDTO) {
        Loan loan = loanRepository.findById(loanPaymentDTO.getLoanId())
                .orElseThrow(() -> new IllegalArgumentException("Loan not found for id: " + loanPaymentDTO.getLoanId()));

        LoanPayment loanPayment = new LoanPayment();
        loanPayment.setLoan(loan);
        loanPayment.setPaidAmount(loanPaymentDTO.getPaidAmount());
        loanPayment.setPaymentDate(loanPaymentDTO.getPaymentDate());

        // Calculate remaining amount after payment
        double remainingAmountAfterPayment = loan.getRemainingAmount() - loanPaymentDTO.getPaidAmount();
        loanPayment.setRemainingAmountAfterPayment(remainingAmountAfterPayment);
        loan.setRemainingAmount(remainingAmountAfterPayment); // Update loan's remaining amount

        LoanPayment savedPayment = loanPaymentRepository.save(loanPayment);
        loanRepository.save(loan); // Save updated loan data

        return convertToDTO(savedPayment);
    }

    // Get all payments for a specific loan
    public List<LoanPaymentDTO> getPaymentsByLoanId(Long loanId) {
        List<LoanPayment> payments = loanPaymentRepository.findByLoan_LoanId(loanId);
        return payments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get a specific payment by paymentId
    public LoanPaymentDTO getPaymentById(Long paymentId) {
        LoanPayment loanPayment = loanPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Loan Payment not found for id: " + paymentId));
        return convertToDTO(loanPayment);
    }

    // Update an existing payment
    public LoanPaymentDTO updateLoanPayment(Long paymentId, LoanPaymentDTO loanPaymentDTO) {
        LoanPayment loanPayment = loanPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Loan Payment not found for id: " + paymentId));

        loanPayment.setPaidAmount(loanPaymentDTO.getPaidAmount());
        loanPayment.setPaymentDate(loanPaymentDTO.getPaymentDate());
        loanPayment.setRemainingAmountAfterPayment(loanPaymentDTO.getRemainingAmountAfterPayment());

        LoanPayment updatedPayment = loanPaymentRepository.save(loanPayment);
        return convertToDTO(updatedPayment);
    }

    // Delete a loan payment by paymentId
    public boolean deleteLoanPayment(Long paymentId) {
        if (loanPaymentRepository.existsById(paymentId)) {
            loanPaymentRepository.deleteById(paymentId);
            return true;
        }
        return false;
    }

    // Helper method to convert LoanPayment entity to LoanPaymentDTO
    private LoanPaymentDTO convertToDTO(LoanPayment loanPayment) {
        LoanPaymentDTO dto = new LoanPaymentDTO();
        dto.setPaymentId(loanPayment.getPaymentId());
        dto.setLoanId(loanPayment.getLoan().getLoanId());
        dto.setPaidAmount(loanPayment.getPaidAmount());
        dto.setPaymentDate(loanPayment.getPaymentDate());
        dto.setRemainingAmountAfterPayment(loanPayment.getRemainingAmountAfterPayment());
        dto.setIssuedAmount(loanPayment.getLoan().getIssuedAmount());
        dto.setEmi(loanPayment.getLoan().getEmi());
        dto.setInterestRate(loanPayment.getLoan().getInterestRate());
        return dto;
    }
}
