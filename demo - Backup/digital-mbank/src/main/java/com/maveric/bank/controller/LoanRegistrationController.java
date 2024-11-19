package com.maveric.bank.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.bank.dto.LoanRegistrationDTO;
import com.maveric.bank.service.LoanRegistrationService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/loan-registration")
public class LoanRegistrationController {
	
	@Autowired
	private Validator validator;


	  @Autowired
	    private LoanRegistrationService loanRegistrationService;

	    @Autowired
	    private ObjectMapper objectMapper; // Jackson ObjectMapper for JSON parsing

	    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<String> createLoanRegistration(
	        @RequestPart("data") String loanRegistrationData,
	        @RequestPart("personalImage") MultipartFile personalImage,
	        @RequestPart("idProofPdf") MultipartFile idProofPdf,
	        @RequestPart("addressProofPdf") MultipartFile addressProofPdf,
	        @RequestPart("bankStatementPdf") MultipartFile bankStatementPdf
	    ) throws IOException {
	        // Convert JSON string to LoanRegistrationDTO
	        LoanRegistrationDTO loanRegistrationDTO = objectMapper.readValue(loanRegistrationData, LoanRegistrationDTO.class);

	        // Validate loanRegistrationDTO 
	        Set<ConstraintViolation<LoanRegistrationDTO>> violations = validator.validate(loanRegistrationDTO);
	        if (!violations.isEmpty()) {
	            StringBuilder errors = new StringBuilder();
	            for (ConstraintViolation<LoanRegistrationDTO> violation : violations) {
	                errors.append(violation.getMessage()).append("; ");
	            }
	            return ResponseEntity.badRequest().body("Validation errors: " + errors.toString());
	        }

	        // File size validation for the uploaded files
	        if (personalImage.getSize() > 256 * 1024) {
	            return ResponseEntity.badRequest().body("Personal image size should be 0 to 256 KB.");
	        }
	        if (idProofPdf.getSize() > 2 * 1024 * 1024) {
	            return ResponseEntity.badRequest().body("ID proof PDF size should be 0 to 2 MB.");
	        }
	        if (addressProofPdf.getSize() > 2 * 1024 * 1024) {
	            return ResponseEntity.badRequest().body("Address proof PDF size should be 0 to 2 MB.");
	        }
	        if (bankStatementPdf.getSize() > 2 * 1024 * 1024) {
	            return ResponseEntity.badRequest().body("Bank statement PDF size should be 0 to 2 MB.");
	        }

	        // Set the uploaded files as byte arrays in the DTO
	        loanRegistrationDTO.setPersonalImage(personalImage.getBytes());
	        loanRegistrationDTO.setIdProofPdf(idProofPdf.getBytes());
	        loanRegistrationDTO.setAddressProofPdf(addressProofPdf.getBytes());
	        loanRegistrationDTO.setBankStatementPdf(bankStatementPdf.getBytes());

	        // Call the service to save the registration details
	        loanRegistrationService.createLoanRegistration(loanRegistrationDTO);

	        // Return a success message
	        return ResponseEntity.status(HttpStatus.CREATED).body("Thank you, registered successfully.");
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<LoanRegistrationDTO> getLoanRegistration(@PathVariable Long id) {
	        LoanRegistrationDTO loanRegistrationDTO = loanRegistrationService.getLoanRegistrationById(id);
	        return ResponseEntity.ok(loanRegistrationDTO);
	    }

	    
	    // Endpoint for downloading a specific file
//	    @GetMapping("/{id}/download/{fileType}")
//	    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id, @PathVariable String fileType) {
//	        LoanRegistrationDTO loanRegistrationDTO = loanRegistrationService.getLoanRegistrationById(id);
//	        byte[] fileData = null;
//	        String fileName = "";
//	        MediaType mediaType = null;
//
//	        switch (fileType.toLowerCase()) {
//	            case "personalimage":
//	                fileData = loanRegistrationDTO.getPersonalImage();
//	                fileName = "personal_image.jpg"; // Set appropriate file name and extension
//	                mediaType = MediaType.IMAGE_JPEG; // Change this based on your image format
//	                break;
//	            case "idproof":
//	                fileData = loanRegistrationDTO.getIdProofPdf();
//	                fileName = "id_proof.pdf";
//	                mediaType = MediaType.APPLICATION_PDF;
//	                break;
//	            case "addressproof":
//	                fileData = loanRegistrationDTO.getAddressProofPdf();
//	                fileName = "address_proof.pdf";
//	                mediaType = MediaType.APPLICATION_PDF;
//	                break;
//	            case "bankstatement":
//	                fileData = loanRegistrationDTO.getBankStatementPdf();
//	                fileName = "bank_statement.pdf";
//	                mediaType = MediaType.APPLICATION_PDF;
//	                break;
//	            default:
//	                return ResponseEntity.badRequest().body(null);
//	        }
//
//	        return ResponseEntity.ok()
//	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//	                .contentType(mediaType)
//	                .body(fileData);
//	    }
	    
	    
	    @GetMapping("/{id}/download/all")
	    public ResponseEntity<byte[]> downloadAllFiles(@PathVariable Long id) {
	        LoanRegistrationDTO loanRegistrationDTO = loanRegistrationService.getLoanRegistrationById(id);
	        
	        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
	             
	            // Add personal image to ZIP
	            if (loanRegistrationDTO.getPersonalImage() != null) {
	                ZipEntry personalImageEntry = new ZipEntry("personal_image.jpg");
	                zipOutputStream.putNextEntry(personalImageEntry);
	                zipOutputStream.write(loanRegistrationDTO.getPersonalImage());
	                zipOutputStream.closeEntry();
	            }

	            // Add ID proof PDF to ZIP
	            if (loanRegistrationDTO.getIdProofPdf() != null) {
	                ZipEntry idProofEntry = new ZipEntry("id_proof.pdf");
	                zipOutputStream.putNextEntry(idProofEntry);
	                zipOutputStream.write(loanRegistrationDTO.getIdProofPdf());
	                zipOutputStream.closeEntry();
	            }

	            // Add address proof PDF to ZIP
	            if (loanRegistrationDTO.getAddressProofPdf() != null) {
	                ZipEntry addressProofEntry = new ZipEntry("address_proof.pdf");
	                zipOutputStream.putNextEntry(addressProofEntry);
	                zipOutputStream.write(loanRegistrationDTO.getAddressProofPdf());
	                zipOutputStream.closeEntry();
	            }

	            // Add bank statement PDF to ZIP
	            if (loanRegistrationDTO.getBankStatementPdf() != null) {
	                ZipEntry bankStatementEntry = new ZipEntry("bank_statement.pdf");
	                zipOutputStream.putNextEntry(bankStatementEntry);
	                zipOutputStream.write(loanRegistrationDTO.getBankStatementPdf());
	                zipOutputStream.closeEntry();
	            }

	            zipOutputStream.finish(); // Finish the ZIP file

	            byte[] zipData = byteArrayOutputStream.toByteArray();

	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"loan_registration_files.zip\"")
	                    .contentType(MediaType.APPLICATION_OCTET_STREAM) // Generic binary stream
	                    .body(zipData);
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	
	    
	    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoanRegistration(@PathVariable Long id) {
        boolean deleted = loanRegistrationService.deleteLoanRegistration(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
