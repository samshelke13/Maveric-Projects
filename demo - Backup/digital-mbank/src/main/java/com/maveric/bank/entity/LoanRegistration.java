package com.maveric.bank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String middleName;
    private String lastName;
    private String fathersName;
    private String mothersMaidenName;
    private String mobileNumber;
    private String phoneNumber;
    private String emailAddress;
    private Integer yearsInCurrentAddress;
    private Integer yearsInCurrentCity;
    private String residenceOwnership;
    private String gender;
    private String maritalStatus;
    private String educationQualification;
    private String occupation;
    private String designation;
    private String department;
    private Boolean isSalaried;
    private String status;
    private LocalDate dateOfBirth;

    private String residenceAddress;
    private String landmark;
    private String country;
    private String state;
    private String city;
    private String pincode;

    // File fields for image and PDFs
    @Lob
    private byte[] addressProofPdf;

    @Lob
    private byte[] bankStatementPdf;

    @Lob
    private byte[] idProofPdf;

    @Lob
    private byte[] personalImage;

}
