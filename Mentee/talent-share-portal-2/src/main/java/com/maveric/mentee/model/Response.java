package com.maveric.mentee.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Response {

	private String message;
	private String errors;
	private int status;
	private LocalDateTime timestamp;
	private String path;
	private Mentee data;

	public Response(String user, String message, int statusCode, LocalDateTime timestamp, String requestURI) {
		this(user, message, statusCode, timestamp, requestURI, null); // Call the other constructor with mentee as null
	}

}
