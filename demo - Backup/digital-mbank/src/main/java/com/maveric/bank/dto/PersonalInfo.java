package com.maveric.bank.dto;

import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfo {

    // Personal Information
    @NotBlank(message = "Name is required")
    private String firstName;
    @NotBlank(message = "Name is required")
    private String middleName;
    @NotBlank(message = "middleName is required")
    private String lastName;
    @NotBlank(message = "fathersName is required")
    private String fathersName;
    @NotBlank(message = "mothersMaidenName is required")
    private String mothersMaidenName;

    @NotNull(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be a 10-digit number")
    private String mobileNumber;
    @NotNull(message = "phoneNumber is required")
    private String phoneNumber;
    @NotNull(message = "emailAddress is required")
    private String emailAddress;
    @NotNull(message = "yearsInCurrentAddress is required")
    private Integer yearsInCurrentAddress;
    @NotNull(message = "yearsInCurrentCity is required")
    private Integer yearsInCurrentCity;
    @NotNull(message = "residenceOwnership is required")
    private String residenceOwnership;
    @NotBlank(message = "gender is required")
    private String gender;
    @NotBlank(message = "maritalStatus is required")
    private String maritalStatus;
    @NotBlank(message = "educationQualification is required")
    private String educationQualification;
    @NotBlank(message = "occupation is required")
    private String occupation;
    @NotBlank(message = "designation is required")
    private String designation;
    @NotBlank(message = "department is required")
    private String department;
    @NotNull(message = "isSalaried is required")
    private Boolean isSalaried;
    @NotBlank(message = "status is required")
    private String status;
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;

    // Address Information
    @NotBlank(message = "residenceAddress is required")
    private String residenceAddress;
    private String landmark;
    private String country;
    private String state;
    private String city;
    @NotNull(message = "pincode is required")
    @Pattern(regexp = "\\d{6}", message = "Pincode must be a 6-digit number")
    private String pincode;


}
