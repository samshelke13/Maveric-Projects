package com.maveric.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maveric.bank.dto.SetLoanDTO;
import com.maveric.bank.entity.SetLoan;
import com.maveric.bank.repository.SetLoanRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetLoanService {

    @Autowired
    private SetLoanRepository setLoanRepository;

    public List<SetLoanDTO> getAllLoans() {
        return setLoanRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SetLoanDTO getLoanById(Long id) {
        SetLoan loan = setLoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        return convertToDTO(loan);
    }

    public SetLoanDTO createLoan(SetLoanDTO setLoanDTO) {
        SetLoan loan = new SetLoan();
        loan.setTitle(setLoanDTO.getTitle());
        loan.setLoanAmount(setLoanDTO.getLoanAmount());
        loan.setLoanTenure(setLoanDTO.getLoanTenure());
        loan.setInterestRate(setLoanDTO.getInterestRate() != null ? setLoanDTO.getInterestRate() : loan.adjustInterestRateByLoanType());

        loan.calculateLoanDetails();

        SetLoan savedLoan = setLoanRepository.save(loan);
        return convertToDTO(savedLoan);
    }

    public SetLoanDTO updateLoan(Long id, SetLoanDTO setLoanDTO) {
        SetLoan loan = setLoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setTitle(setLoanDTO.getTitle());
        loan.setLoanAmount(setLoanDTO.getLoanAmount());
        loan.setLoanTenure(setLoanDTO.getLoanTenure());
        loan.setInterestRate(setLoanDTO.getInterestRate());

        loan.calculateLoanDetails();

        SetLoan updatedLoan = setLoanRepository.save(loan);
        return convertToDTO(updatedLoan);
    }

    public void deleteLoan(Long id) {
        setLoanRepository.deleteById(id);
    }

    private SetLoanDTO convertToDTO(SetLoan loan) {
        return new SetLoanDTO(
                loan.getId(),
                loan.getTitle(),
                loan.getLoanAmount(),
                loan.getLoanTenure(),
                loan.getInterestRate(),
                loan.getCalculatedEMI(),
                loan.getTotalInterestAmount(),
                loan.getTotalAmountPayable(),
                loan.getTitleDescription()
        );
    }
}
