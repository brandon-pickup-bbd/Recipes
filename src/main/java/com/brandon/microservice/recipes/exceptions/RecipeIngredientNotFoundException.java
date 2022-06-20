package com.brandon.microservice.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecipeIngredientNotFoundException extends RuntimeException {

	public RecipeIngredientNotFoundException(String message) {
		super(message);
	}

}