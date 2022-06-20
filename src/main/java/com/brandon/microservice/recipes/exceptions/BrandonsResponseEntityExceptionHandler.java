package com.brandon.microservice.recipes.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class BrandonsResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) throws Exception {
		return new ResponseEntity(getGenericExceptionResponse(ex.getMessage(), request.getDescription(false)),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RecipeNotFoundException.class)
	public final ResponseEntity<Object> handleRecipeNotFoundException(Exception ex, WebRequest request)
			throws Exception {
		return new ResponseEntity(getGenericExceptionResponse(ex.getMessage(), request.getDescription(false)),
				HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity(getGenericExceptionResponse("Validation Failed", ex.getBindingResult().toString()),
				HttpStatus.BAD_REQUEST);
	}

	private ExceptionResponse getGenericExceptionResponse(String message, String details) {
		return new ExceptionResponse(new Date(), message, details);
	}

}
