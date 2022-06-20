package com.brandon.microservice.recipes.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.brandon.microservice.recipes.models.Recipe;
import com.brandon.microservice.recipes.models.RecipeIngredient;
import com.brandon.microservice.recipes.services.RecipeService;

@RestController
public class RecipeController {

	@Autowired
	private RecipeService recipeService;

	@GetMapping(path = "/recipes", name = "GetRecipes")
	public List<Recipe> getRecipes(@RequestParam(required = false) Boolean isVegetarian,
			@RequestParam(required = false) String includesIngredient,
			@RequestParam(required = false) String excludesIngredient,
			@RequestParam(required = false) String instructionContains) {

		return recipeService.getRecipes(isVegetarian, includesIngredient, excludesIngredient, instructionContains);
	}

	@PostMapping(path = "/recipes")
	public ResponseEntity<Object> createRecipe(@Valid @RequestBody Recipe recipe) {
		return recipeService.addRecipe(recipe);
	}

	@GetMapping(path = "/recipes/{id}")
	public Recipe getRecipeById(@PathVariable int id) {
		return recipeService.getRecipe(id);
	}

	@PutMapping(path = "/recipes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateRecipeById(@PathVariable int id, @RequestBody Recipe recipe) {
		recipeService.updateRecipe(id, recipe);
	}

	@DeleteMapping(path = "/recipes/{id}")
	public void deleteRecipeById(@PathVariable int id) {
		recipeService.removeRecipe(id);
	}

	@GetMapping(path = "/recipes/{id}/servings")
	public int getServingsByRecipeById(@PathVariable int id) {
		return recipeService.getServingsForRecipe(id);
	}

	@GetMapping(path = "/recipes/{id}/ingredients")
	public List<RecipeIngredient> getRecipeIngredients(@PathVariable int id) {
		return recipeService.getRecipeIngredients(id);
	}

	@PostMapping(path = "/recipes/{id}/ingredients")
	public ResponseEntity<Object> addIngredientToRecipe(@PathVariable int id,
			@RequestBody RecipeIngredient recipeIngredient) {
		return recipeService.addIngredientToRecipe(id, recipeIngredient);
	}

	@PutMapping(path = "/recipes/{id}/ingredients/{recipeIngredientId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateRecipeIngredient(@PathVariable int id, @PathVariable int recipeIngredientId,
			@RequestBody RecipeIngredient recipeIngredient) {

		recipeService.updateRecipeIngredient(id, recipeIngredientId, recipeIngredient);
	}

	@DeleteMapping(path = "/recipes/{id}/ingredients/{recipeIngredientId}")
	public void deleteIngredientFromRecipe(@PathVariable int id, @PathVariable int recipeIngredientId) {
		recipeService.removeIngredientFromRecipe(id, recipeIngredientId);
	}

}
