package com.maveric.bank.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.bank.entity.Branch;
import com.maveric.bank.service.BranchService;

@RestController
@RequestMapping("/branches")
public class BranchController {

	@Autowired
	private BranchService branchService;

	@PostMapping("/create")
	public ResponseEntity<String> createBranch(@RequestBody Branch branch) {
		Branch createdBranch = branchService.createBranch(branch);
		return new ResponseEntity<>("Branch successfully created with ID: " + createdBranch.getBranchId(),
				HttpStatus.CREATED);
	}

	@GetMapping
	public List<Branch> getAllBranches() {
		return branchService.getAllBranches();
	}

	@GetMapping("/{branchId}")
	public ResponseEntity<Branch> getBranchById(@PathVariable String branchId) {
		return Optional.ofNullable(branchService.getBranchById(branchId))
				.map(branch -> new ResponseEntity<>(branch, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/{branchId}")
	public ResponseEntity<Void> deleteBranch(@PathVariable String branchId) {
		branchService.deleteBranch(branchId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
