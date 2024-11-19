package com.maveric.mentee.exception;


	
	import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.mongodb.MongoWriteException;

import jakarta.servlet.http.HttpServletRequest;

	@ExtendWith(MockitoExtension.class)
	public class ApplicationExceptionHandlerTest {

	    @InjectMocks
	    private ApplicationExceptionHandler exceptionHandler;

	    @Mock
	    private HttpServletRequest request;

	   
	    
	    @Test
	    public void testHandleWrongCredentialsException() {
	        WrongCredentialsExeption ex = new WrongCredentialsExeption("Invalid credentials");
	        
	        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handeleInvalidCredentials(ex, request);
	        
	        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
	        assertNotNull(responseEntity.getBody());
	        assertEquals(responseEntity.getBody().getStatus(), 400);
	        assertEquals(responseEntity.getBody().getErrors(), "Invalid Credentials");
	        assertEquals(responseEntity.getBody().getMessage(), "Invalid credentials");
	        assertNotNull(responseEntity.getBody().getTimestamp());
	        assertEquals(responseEntity.getBody().getPath(), request.getRequestURI());
	    }

	  

	   



	
}
