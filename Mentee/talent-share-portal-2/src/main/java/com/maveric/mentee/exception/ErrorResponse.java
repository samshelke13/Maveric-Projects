package com.maveric.mentee.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	private int status;
	private String errors;
	private String message;
	private LocalDateTime timestamp;
	private String path;

}
