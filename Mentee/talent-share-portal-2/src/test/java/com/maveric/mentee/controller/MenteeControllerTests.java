package com.maveric.mentee.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.maveric.mentee.constance.Constants;
import com.maveric.mentee.exception.WrongCredentialsExeption;
import com.maveric.mentee.model.Mentee;
import com.maveric.mentee.model.Response;
import com.maveric.mentee.service.MenteeService;

import jakarta.servlet.http.HttpServletRequest;

@SpringBootTest(classes = { MenteeControllerTests.class })
public class MenteeControllerTests {
	
	@InjectMocks
	private MenteeController controller;

	@Mock
	private MenteeService service;
	
	 @Mock
	 private HttpServletRequest request;
	 
	@Test
    public void testRegisterMentee() {
	Mentee mentee = new Mentee();
	
    // Set properties as needed
	mentee.setEmployeeId(1);
	mentee.setFirstName("Ram");
	mentee.setLastName("SHAM");
	mentee.setEmail("ram@gmail.com");
	mentee.setPassword("123456789");
	mentee.setCreatedAt(LocalDateTime.now());
	mentee.setActive(true);
	
	Response response=new Response();
	response.setData(mentee);
	response.setErrors(null);
	response.setMessage("Record inserted successfully");
	response.setStatus(200);
	response.setPath(request.getRequestURI());
	response.setTimestamp(LocalDateTime.now());

    when(service.registerMentee(mentee,request)).thenReturn(response);


    ResponseEntity<Object> result = controller.registerMentee(mentee,request);

    verify(service).registerMentee(mentee,request);

    assertEquals(HttpStatus.OK, result.getStatusCode());
//    assertEquals("Record inserted successfully", result.getBody());
//    assertNull(((Response) result.getBody()).getErrors());
//    assertEquals(mentee, ((Response) result.getBody()).getData());
//    assertEquals(200, ((Response) result.getBody()).getStatus());
}
	

	@Test
    public void testLogInMenteeValidCredentials() throws WrongCredentialsExeption {
        int userId = 123;
        String password = "validPassword";
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(Constants.VALID_USER, HttpStatus.OK);

        when(service.logInMentee(userId, password,request)).thenReturn(responseEntity);

        ResponseEntity<Object> response = controller.logInMentee(userId, password,request);

        verify(service, times(1)).logInMentee(userId, password,request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Constants.VALID_USER, response.getBody());
    }
	
	@Test
    public void testViewMenteeDetails() {
        int userId = 1; // Replace with a valid userId
        
        Mentee mentee = new Mentee();
    	
        // Set properties as needed
    	mentee.setEmployeeId(1);
    	mentee.setFirstName("Ram");
    	mentee.setLastName("SHAM");
    	mentee.setEmail("ram@gmail.com");
    	mentee.setPassword("123456789");
    	mentee.setCreatedAt(LocalDateTime.now());
    	mentee.setActive(true);
    	
    	Response response=new Response();
    	response.setData(mentee);
    	response.setErrors(null);
    	response.setMessage("Record inserted successfully");
    	response.setStatus(200);
    	response.setPath(request.getRequestURI());
    	response.setTimestamp(LocalDateTime.now());
        HttpServletRequest request = mock(HttpServletRequest.class);
        //Response expectedResponse = new Response(/* your response parameters here */);

        when(service.viewMentee(userId, request)).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<Response> responseEntity = controller.viewMenteeDetails(userId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());

        verify(service).viewMentee(userId, request);
        verifyNoMoreInteractions(service);
    }

	
	
}
