package com.brandon.microservice.recipes.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.brandon.microservice.recipes.exceptions.RecipeFilterNotValidException;
import com.brandon.microservice.recipes.exceptions.RecipeIngredientNotFoundException;
import com.brandon.microservice.recipes.exceptions.RecipeNotFoundException;
import com.brandon.microservice.recipes.models.Recipe;
import com.brandon.microservice.recipes.models.RecipeIngredient;
import com.brandon.microservice.recipes.repositories.RecipeIngredientRepository;
import com.brandon.microservice.recipes.repositories.RecipeRepository;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private RecipeIngredientRepository recipeIngredientRepository;

	@Override
	public List<Recipe> getRecipes(Boolean isVegetarian, String includeIngredient, String excludeIngredient,
			String instructionMatch) {
		if (includeIngredient != null && includeIngredient.equals(excludeIngredient))
			throw new RecipeFilterNotValidException("Include and Exclude ingredient params must be different");

		// Close your eyes - I need some search criteria pattern but I'm outta time
		List<Recipe> matchingRecipes = isVegetarian == null && instructionMatch == null ? recipeRepository.findAll() 
				:  instructionMatch != null && isVegetarian != null ? recipeRepository.findAllByisVegetarianAndInstructionsContains(isVegetarian, instructionMatch) 
				: isVegetarian != null ? recipeRepository.findAllByisVegetarian(isVegetarian) 
				: recipeRepository.findAllByinstructionsContains(instructionMatch);
		
		Iterator<Recipe> iterator = matchingRecipes.iterator();
		while (iterator.hasNext()) {
			Recipe recipe = iterator.next();
			List<String> ingredients = recipe.getIngredients().stream().map(RecipeIngredient::getDescription)
					.collect(Collectors.toList());
			if (excludeIngredient != null && ingredients.contains(excludeIngredient)) {
				iterator.remove();
				continue;
			}
			if (includeIngredient != null && !ingredients.contains(includeIngredient)) {
				iterator.remove();
				continue;
			}
		}
		return matchingRecipes;
	}

	@Override
	public Recipe getRecipe(Integer recipeId) {
		Optional<Recipe> recipe = recipeRepository.findById(recipeId);
		if (!recipe.isPresent())
			throw new RecipeNotFoundException("id-" + recipeId);
		return recipe.get();
	}

	@Override
	public ResponseEntity<Object> addRecipe(Recipe recipe) {
		Recipe newRecipe = recipeRepository.save(recipe);
		for (RecipeIngredient ingredient : recipe.getIngredients()) {
			ingredient.setRecipe(newRecipe);
			recipeIngredientRepository.save(ingredient);
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newRecipe.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@Override
	public void updateRecipe(Integer recipeId, Recipe recipe) {
		Recipe recipeReference = recipeRepository.getReferenceById(recipeId);
		if (recipeReference == null)
			throw new RecipeNotFoundException("id-" + recipeId);
		recipeReference.setDescription(recipe.getDescription());
		recipeReference.setInstructions(recipe.getInstructions());
		recipeReference.setPeopleServed(recipe.getPeopleServed());
		recipeReference.setIsVegetarian(recipe.getIsVegetarian());
		recipeReference.setPreparationTime(recipe.getPreparationTime());

		recipeRepository.save(recipeReference);

	}

	@Override
	public void removeRecipe(Integer recipeId) {
		Optional<Recipe> recipe = recipeRepository.findById(recipeId);
		if (!recipe.isPresent())
			throw new RecipeNotFoundException("id-" + recipeId);

		for (RecipeIngredient recipeIngredient : recipe.get().getIngredients()) {
			recipeIngredientRepository.delete(recipeIngredient);
		}
		recipeRepository.deleteById(recipeId);

	}

	@Override
	public Integer getServingsForRecipe(Integer recipeId) {
		Optional<Recipe> recipe = recipeRepository.findById(recipeId);
		if (!recipe.isPresent())
			throw new RecipeNotFoundException("id-" + recipeId);
		return recipe.get().getPeopleServed();
	}

	@Override
	public List<RecipeIngredient> getRecipeIngredients(Integer recipeId) {
		Optional<Recipe> recipe = recipeRepository.findById(recipeId);
		if (!recipe.isPresent())
			throw new RecipeNotFoundException("id-" + recipeId);

		return recipe.get().getIngredients();
	}

	@Override
	public ResponseEntity<Object> addIngredientToRecipe(Integer recipeId, RecipeIngredient recipeIngredient) {

		Optional<Recipe> recipe = recipeRepository.findById(recipeId);
		if (!recipe.isPresent())
			throw new RecipeNotFoundException("id-" + recipeId);
		recipeIngredient.setRecipe(recipe.get());

		RecipeIngredient newRecipeIngredient = recipeIngredientRepository.save(recipeIngredient);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newRecipeIngredient.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@Override
	public void updateRecipeIngredient(Integer recipeId, Integer recipeIngredientId,
			RecipeIngredient recipeIngredient) {
		Boolean recipeExists = recipeRepository.existsById(recipeId);
		if (!recipeExists)
			throw new RecipeNotFoundException("id-" + recipeId);

		RecipeIngredient recipeIngredientReference = recipeIngredientRepository.getReferenceById(recipeIngredientId);
		if (recipeIngredientReference == null)
			throw new RecipeIngredientNotFoundException("recipeIngredientId-" + recipeIngredientId);

		recipeIngredientReference.setDescription(recipeIngredientReference.getDescription());
		recipeIngredientReference.setIngredientQuantity(recipeIngredient.getIngredientQuantity());

		recipeIngredientRepository.save(recipeIngredientReference);

	}

	@Override
	public void removeIngredientFromRecipe(Integer recipeId, Integer recipeIngredientId) {
		Boolean recipeExists = recipeRepository.existsById(recipeId);
		if (!recipeExists)
			throw new RecipeNotFoundException("id-" + recipeId);

		Boolean recipeIngredientExists = recipeIngredientRepository.existsById(recipeIngredientId);
		if (!recipeIngredientExists)
			throw new RecipeIngredientNotFoundException("recipeIngredientId-" + recipeIngredientId);

		recipeIngredientRepository.deleteById(recipeIngredientId);

	}

}
