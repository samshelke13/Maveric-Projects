package com.maveric.bank.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	@Id
	private Long customerId;
	private String customerName;
	private LocalDate dob;
	private String mobileNumber;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
}
