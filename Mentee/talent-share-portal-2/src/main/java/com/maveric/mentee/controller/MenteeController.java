package com.maveric.mentee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.mentee.constance.Constants;
import com.maveric.mentee.model.Mentee;
import com.maveric.mentee.model.Response;
import com.maveric.mentee.service.MenteeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING)
@Slf4j
@CrossOrigin(origins="*")
public class MenteeController {

	@Autowired
	MenteeService menteeService;


	@Operation(

			description = "This api validates mentee data and registers it in database", summary = "register mentee to the database", responses = {
					@ApiResponse(description = "Successfully added", responseCode = "200"),
					@ApiResponse(description = "Duplicate username or email", responseCode = "400"),
					@ApiResponse(description = "Internal Server Error", responseCode = "500") })
	@PostMapping(Constants.POST_MAPPING)
	public ResponseEntity<Object> registerMentee(@RequestBody @Valid Mentee mentee, HttpServletRequest request) {
		log.info("Entering into controller method registerMentee");
		Response response = menteeService.registerMentee(mentee, request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(description = "validates mentee userName and password", summary = "checks if mentee credentials are valid", responses = {
			@ApiResponse(description = "Valid User, Login Successfully", responseCode = "200"),
			@ApiResponse(description = "Unauthorised User", responseCode = "400"),
			@ApiResponse(description = "Internal Server Error", responseCode = "500")})
	@GetMapping("/login")
	public ResponseEntity<Object> logInMentee(@RequestParam("userId") int userId,
			@RequestParam("password") String password, HttpServletRequest request) {
		log.info("Entering into Controller method loginMentee()");

		ResponseEntity<Object> loginStatus = menteeService.logInMentee(userId, password, request);
		return loginStatus;

	}
	
	 @Operation(description = "validates mentee userName and password", summary = "checks if mentee data is present in database", responses = {
	           @ApiResponse(description = "Record Found Successfully", responseCode = "200"),
	           @ApiResponse(description = " Rocord Not Found", responseCode = "400") })
	   @GetMapping("/{userId}")
	   public ResponseEntity<Response> viewMenteeDetails(@PathVariable("userId")int userId, HttpServletRequest httpRequest) {
	       log.info("Entering into database");
	       // menteeService.registerMentee(mentee);
	       ResponseEntity<Response> response=menteeService.viewMentee(userId,httpRequest);
	       return response;
	       
	       
	   }




}