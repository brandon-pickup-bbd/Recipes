package com.brandon.microservice.recipes.exceptions;

import java.util.Date;

public class ExceptionResponse {
	private String message;
	private Date timestamp;
	private String details;

	public ExceptionResponse(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public String getMessage() {
		return message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getDetails() {
		return details;
	}

}
