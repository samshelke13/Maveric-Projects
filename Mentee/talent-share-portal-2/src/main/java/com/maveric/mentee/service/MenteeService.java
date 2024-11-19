package com.maveric.mentee.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.maveric.mentee.config.SecurityConfig;
import com.maveric.mentee.constance.Constants;
import com.maveric.mentee.exception.WrongCredentialsExeption;
import com.maveric.mentee.model.Mentee;
import com.maveric.mentee.model.Response;
import com.maveric.mentee.repository.MenteeRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MenteeService {

	@Autowired
	MenteeRepository menteeRepository;

	@Autowired
	SecurityConfig securityConfig;

	public Response registerMentee(Mentee mentee, HttpServletRequest request) {

		log.info("Executing service method registerMentee()");
		String encryptedPassword = securityConfig.encryptPassword(mentee.getPassword());
		mentee.setPassword(encryptedPassword);
		mentee.setActive(true);
		mentee.setCreatedAt(LocalDateTime.now());
		Response response = new Response("Record inserted successfully", null, 200, LocalDateTime.now(),
				request.getRequestURI(), mentee);

		menteeRepository.save(mentee);
		log.info("Mentee register successfully");
		return response;
	}

	public ResponseEntity<Object> logInMentee(int userId, String password, HttpServletRequest request)
			throws WrongCredentialsExeption {

		log.info("Executing service method loginMentee()");
		

		Optional<Mentee> mentee = menteeRepository.findByEmployeeId(userId);

		if (mentee.isPresent()) {
			if (securityConfig.matchPassword(password, mentee.get().getPassword())) {
				log.info("login successfully");
				Response response = new Response(Constants.VALID_USER, null, 200, LocalDateTime.now(),
						request.getRequestURI(), null);
				return ResponseEntity.ok(response);
				// return ResponseEntity.ok(Constants.VALID_USER);
			} else {
				throw new WrongCredentialsExeption(Constants.WRONG_PASSWORD);
		
				// ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Constants.WRONG_PASSWORD);
			}
		} else {
			throw new WrongCredentialsExeption(Constants.WRONG_USERID);
			
			// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.WRONG_USERID);
		}

	}

	public ResponseEntity<Response> viewMentee(Integer userId, HttpServletRequest request){
		//throws NoSuchElementException 

	Optional<Mentee> mentee = menteeRepository.findByEmployeeId(userId);

	if (mentee.isPresent()) {
		Response response = new Response("View Mentee Record", null, 200, LocalDateTime.now(),
				request.getRequestURI(), mentee.get());
		log.info("View Mentee Record");
		return ResponseEntity.ok(response);
	} else {
		log.info("mentee not found");
		throw new NoSuchElementException(Constants.INVALID_USER);
	}
}


}
