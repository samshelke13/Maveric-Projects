package com.talentshare.mentor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentshare.mentor.dto.ResponseDetails;
import com.talentshare.mentor.service.SkillService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/skill")
@CrossOrigin(origins="*")
public class SkillController {
	
	@Autowired
	SkillService skillService;

	@Operation(
			summary="Retrieves list of skills",
			description="Retrives all the skills from Database",
			responses= {
					@ApiResponse(
							description = "Valid User",
							responseCode = "200"
							),
					@ApiResponse(
							description = "Unauthorised User",
							responseCode = "400"
							),
					@ApiResponse(
							description = "Internal Server Error",
							responseCode = "500"
							)
			})
	@GetMapping("/get-all-skills")
	public ResponseEntity<ResponseDetails> showAllMentor(HttpServletRequest httpRequest ){
		
		ResponseDetails response= skillService.getAllSkills(httpRequest);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
}
