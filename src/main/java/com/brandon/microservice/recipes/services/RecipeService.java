package com.brandon.microservice.recipes.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.brandon.microservice.recipes.models.Recipe;
import com.brandon.microservice.recipes.models.RecipeIngredient;

public interface RecipeService {
	List<Recipe> getRecipes(Boolean isVegetarian, String includeIngredient, String excludeIngredient, String instructionMatch);

	Recipe getRecipe(Integer recipeId);

	ResponseEntity<Object> addRecipe(Recipe recipe);

	void updateRecipe(Integer recipeId, Recipe recipe);

	void removeRecipe(Integer recipeId);

	Integer getServingsForRecipe(Integer recipeId);

	List<RecipeIngredient> getRecipeIngredients(Integer recipeId);

	ResponseEntity<Object> addIngredientToRecipe(Integer recipeId, RecipeIngredient recipeIngredient);
	
	void updateRecipeIngredient(Integer recipeId, Integer recipeIngredientId, RecipeIngredient recipeIngredient);
	
	void removeIngredientFromRecipe(Integer recipeId, Integer recipeIngredientId);
}
