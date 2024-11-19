package com.maveric.mentee.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.maveric.mentee.config.SecurityConfig;
import com.maveric.mentee.constance.Constants;
import com.maveric.mentee.exception.WrongCredentialsExeption;
import com.maveric.mentee.model.Mentee;
import com.maveric.mentee.model.Response;
import com.maveric.mentee.repository.MenteeRepository;

import jakarta.servlet.http.HttpServletRequest;

@SpringBootTest(classes = { MenteeServiceTests.class })
public class MenteeServiceTests {

	@InjectMocks
	private MenteeService service;

	@Mock
	private MenteeRepository repository;

	@Mock
	private SecurityConfig securityConfig;

	@Mock
	private HttpServletRequest request;

	@Test
	public void testRegisterMentee() {
		Mentee mentee = new Mentee();
		mentee.setEmployeeId(1);
		mentee.setFirstName("Ram");
		mentee.setLastName("Sham");
		mentee.setEmail("ram@gmail.com");
		mentee.setPassword("123456789");
		mentee.setCreatedAt(null);
		mentee.setActive(true);

		when(securityConfig.encryptPassword(anyString())).thenReturn("encryptedPassword");

		when(repository.save(any(Mentee.class))).thenReturn(mentee);

		Response response = service.registerMentee(mentee, request);

		verify(repository, times(1)).save(mentee);

		assertEquals("Record inserted successfully", response.getMessage());
		assertEquals(200, response.getStatus());
		assertEquals(mentee, response.getData());
	}

	@Test
	public void testLogInMenteeValidCredentials() throws WrongCredentialsExeption {
		int userId = 123;
		String password = "validPassword";
		Optional<Mentee> mentee = Optional.of(new Mentee());

		mentee.get().setEmployeeId(userId);
		mentee.get().setPassword("encryptedPassword");

		when(repository.findByEmployeeId(userId)).thenReturn(mentee);
		when(securityConfig.matchPassword(password, mentee.get().getPassword())).thenReturn(true);

		ResponseEntity<Object> response = service.logInMentee(userId, password, request);

		verify(repository, times(1)).findByEmployeeId(userId);
		verify(securityConfig, times(1)).matchPassword(password, mentee.get().getPassword());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		// assertEquals(Constants.VALID_USER, response.getBody());
	}

	@Test
	public void testLogInMenteeWrongPassword() {
		int userId = 123;
		String password = "invalidPassword";
		Optional<Mentee> mentee = Optional.of(new Mentee());
		mentee.get().setEmployeeId(userId);
		mentee.get().setPassword("encryptedPassword");

		when(repository.findByEmployeeId(userId)).thenReturn(mentee);
		when(securityConfig.matchPassword(password, mentee.get().getPassword())).thenReturn(false);

		assertThrows(WrongCredentialsExeption.class, () -> {
			service.logInMentee(userId, password, request);
		});

		verify(repository, times(1)).findByEmployeeId(userId);
		verify(securityConfig, times(1)).matchPassword(password, mentee.get().getPassword());
	}

	@Test
	public void testLogInMenteeWrongUserId() {
		int userId = 123;
		String password = "validPassword";

		when(repository.findByEmployeeId(userId)).thenReturn(Optional.empty());

		assertThrows(WrongCredentialsExeption.class, () -> {
			service.logInMentee(userId, password, request);
		});

		verify(repository, times(1)).findByEmployeeId(userId);

	}
	
	@Test
	public void testViewMentee() {
		int userId = 1;
		Mentee mentee = new Mentee();
		mentee.setEmployeeId(userId);
		mentee.setFirstName("Ram");
		mentee.setLastName("Sham");
		mentee.setEmail("ram@gmail.com");
		mentee.setPassword("123456789");
		mentee.setCreatedAt(null);
		mentee.setActive(true);
		Optional<Mentee> optionalMentee = Optional.of(mentee);

		when(repository.findByEmployeeId(userId)).thenReturn(optionalMentee);

		ResponseEntity<Response> responseEntity = service.viewMentee(userId, request);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		Response response = new Response();
		response.setData(mentee);
		response.setErrors(null);
		response.setMessage("View Mentee Record");
		response.setStatus(200);
		response.setPath(request.getRequestURI());
		response.setTimestamp(LocalDateTime.now());
		assertEquals(response.getMessage(), responseEntity.getBody().getMessage());

		verify(repository).findByEmployeeId(userId);
		verifyNoMoreInteractions(repository);
	}

	@Test
	void testViewMenteeNotFound() {
		int userId = 123;
		when(repository.findByEmployeeId(userId)).thenReturn(Optional.empty());
		
		when(request.getRequestURI()).thenReturn("/FWEF");
		assertThrows(NoSuchElementException.class, () -> {service.viewMentee(userId, request);});
		// assertEquals("No value present", exception.getMessage());
		// verify(repository).findByEmployeeId(userId);
		verify(repository, times(1)).findByEmployeeId(userId);
	}


}
