package com.maveric.mentee.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mongodb.MongoWriteException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

	ErrorResponse errorResponse;

	@ExceptionHandler(MongoWriteException.class)
	public ResponseEntity<ErrorResponse> handleduplicateKey(MongoWriteException ex, HttpServletRequest request) {

		boolean message = ex.getError().getMessage().contains("employeeId dup key");
		String error = "Duplicate key Error";
		if (message == true) {
			ErrorResponse errorResponse = new ErrorResponse(400, error, "Employee Id Alredy Exist", LocalDateTime.now(),
					request.getRequestURI());
			log.warn("Employee Id Alredy Exist");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		} else {
			ErrorResponse errorResponse = new ErrorResponse(400, error, "Emai Id Alredy Exist", LocalDateTime.now(),
					request.getRequestURI());
			log.warn("Emai Id Alredy Exist");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@ExceptionHandler(WrongCredentialsExeption.class)
	public ResponseEntity<ErrorResponse> handeleInvalidCredentials(WrongCredentialsExeption ex,
			HttpServletRequest request) {
		log.warn("Invalid Credentials");
		ErrorResponse errorResponse = new ErrorResponse(400, "Invalid Credentials", ex.getMessage(),
				LocalDateTime.now(), request.getRequestURI());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}



	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest req) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<String> errors = new ArrayList<>();
		for (FieldError fieldError : fieldErrors) {
			errors.add(fieldError.getDefaultMessage());
		}
		String errorMessage = String.join("; ", errors);
		log.warn("Validation Errors");
		ErrorResponse errorResponse = new ErrorResponse(400, "Validation Errors", errorMessage, LocalDateTime.now(),
				req.getRequestURI());
		return ResponseEntity.status(400).body(errorResponse);

	}

	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(NoSuchElementException ex,
			HttpServletRequest request) {
		log.warn("User Not Found");
		ErrorResponse errorResponse = new ErrorResponse(400, "User Not Found", ex.getMessage(), LocalDateTime.now(),
				request.getRequestURI());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}


}