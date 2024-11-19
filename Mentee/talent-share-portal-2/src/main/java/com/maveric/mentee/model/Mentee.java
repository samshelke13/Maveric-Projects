package com.maveric.mentee.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Document(collection = "MenteeProfile")
public class Mentee {

//	
	// @NotBlank(message="Employee Id should not be null")
	@NotNull(message = "Employee Id should not be null")
	// @JsonFilter("exclude0OrNullFilter")
	@Indexed(unique = true)
	private Integer employeeId;

	@NotBlank(message = "First name is required")
	private String firstName;

	@NotBlank(message = "Last name is required")
	private String lastName;

	@Email(message = "Invalid email format")
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]{3,}@[a-zA-Z0-9.-]+$", message = "Invalid email format.")
	@Indexed(unique = true)
	private String email;

	@Size(min = 6, message = "Password must be at least 6 characters long")

	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",

			message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
	@NotBlank(message = "Password is Required")

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Use this annotation to exclude the field during
															// serialization

	private String password;
	private LocalDateTime createdAt;

	// @NotBlank(message = "isActive is required")
	private boolean isActive;
}
