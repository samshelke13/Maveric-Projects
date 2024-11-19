package com.maveric.bank.service;

import com.maveric.bank.dto.LoanRegistrationDTO;
import com.maveric.bank.entity.LoanRegistration;
import com.maveric.bank.repository.LoanRegistrationRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanRegistrationService {

    @Autowired
    private LoanRegistrationRepository loanRegistrationRepository;

    public LoanRegistrationDTO createLoanRegistration(LoanRegistrationDTO dto) throws IOException {
        LoanRegistration entity = convertToEntity(dto);
        LoanRegistration savedEntity = loanRegistrationRepository.save(entity);
        return convertToDTO(savedEntity);
    }
    

    public List<LoanRegistrationDTO> getAllLoanRegistrations() {
        return loanRegistrationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LoanRegistrationDTO getLoanRegistrationById(Long id) {
        Optional<LoanRegistration> registration = loanRegistrationRepository.findById(id);
        return registration.map(this::convertToDTO).orElse(null);
    }

    public boolean deleteLoanRegistration(Long id) {
        if (loanRegistrationRepository.existsById(id)) {
            loanRegistrationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private LoanRegistration convertToEntity(LoanRegistrationDTO dto) throws IOException {
        LoanRegistration entity = new LoanRegistration();
        // Set other fields...
        
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setFathersName(dto.getFathersName());
        entity.setMothersMaidenName(dto.getMothersMaidenName());
        entity.setMobileNumber(dto.getMobileNumber());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmailAddress(dto.getEmailAddress());
        entity.setYearsInCurrentAddress(dto.getYearsInCurrentAddress());
        entity.setYearsInCurrentCity(dto.getYearsInCurrentCity());
        entity.setResidenceOwnership(dto.getResidenceOwnership());
        entity.setGender(dto.getGender());
        entity.setMaritalStatus(dto.getMaritalStatus());
        entity.setEducationQualification(dto.getEducationQualification());
        entity.setOccupation(dto.getOccupation());
        entity.setDesignation(dto.getDesignation());
        entity.setDepartment(dto.getDepartment());
        entity.setIsSalaried(dto.getIsSalaried());
        entity.setStatus(dto.getStatus());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setResidenceAddress(dto.getResidenceAddress());
        entity.setLandmark(dto.getLandmark());
        entity.setCountry(dto.getCountry());
        entity.setState(dto.getState());
        entity.setCity(dto.getCity());
        entity.setPincode(dto.getPincode());

        // Set file data
        if (dto.getPersonalImage() != null) {
            entity.setPersonalImage(dto.getPersonalImage());
        }
        if (dto.getIdProofPdf() != null) {
            entity.setIdProofPdf(dto.getIdProofPdf());
        }
        if (dto.getAddressProofPdf() != null) {
            entity.setAddressProofPdf(dto.getAddressProofPdf());
        }
        if (dto.getBankStatementPdf() != null) {
            entity.setBankStatementPdf(dto.getBankStatementPdf());
        }

        return entity;
    }

    private LoanRegistrationDTO convertToDTO(LoanRegistration entity) {
        LoanRegistrationDTO dto = new LoanRegistrationDTO();
        // Set other fields...
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setFathersName(entity.getFathersName());
        dto.setMothersMaidenName(entity.getMothersMaidenName());
        dto.setMobileNumber(entity.getMobileNumber());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmailAddress(entity.getEmailAddress());
        dto.setYearsInCurrentAddress(entity.getYearsInCurrentAddress());
        dto.setYearsInCurrentCity(entity.getYearsInCurrentCity());
        dto.setResidenceOwnership(entity.getResidenceOwnership());
        dto.setGender(entity.getGender());
        dto.setMaritalStatus(entity.getMaritalStatus());
        dto.setEducationQualification(entity.getEducationQualification());
        dto.setOccupation(entity.getOccupation());
        dto.setDesignation(entity.getDesignation());
        dto.setDepartment(entity.getDepartment());
        dto.setIsSalaried(entity.getIsSalaried());
        dto.setStatus(entity.getStatus());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setResidenceAddress(entity.getResidenceAddress());
        dto.setLandmark(entity.getLandmark());
        dto.setCountry(entity.getCountry());
        dto.setState(entity.getState());
        dto.setCity(entity.getCity());
        dto.setPincode(entity.getPincode());
        dto.setAddressProofPdf(entity.getAddressProofPdf());
        dto.setBankStatementPdf(entity.getBankStatementPdf());
        dto.setIdProofPdf(entity.getIdProofPdf());
        dto.setPersonalImage(entity.getPersonalImage());
        
        return dto;
    }
}