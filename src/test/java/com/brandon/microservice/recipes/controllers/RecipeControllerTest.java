package com.brandon.microservice.recipes.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.brandon.microservice.recipes.models.Recipe;
import com.brandon.microservice.recipes.models.RecipeIngredient;
import com.brandon.microservice.recipes.services.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

	@MockBean
	RecipeService recipeService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private List<Recipe> recipes;

	@BeforeEach
	public void init() {

		RecipeIngredient ingredient1 = new RecipeIngredient(1, "Milk", "500ml");
		RecipeIngredient ingredient2 = new RecipeIngredient(2, "Eggs", "2");
		RecipeIngredient ingredient3 = new RecipeIngredient(3, "Sugar", "200g");
		RecipeIngredient ingredient4 = new RecipeIngredient(4, "Butter", "40g");
		RecipeIngredient ingredient5 = new RecipeIngredient(5, "Oil", "1tsp");
		RecipeIngredient ingredient6 = new RecipeIngredient(6, "Water", "200ml");

		List<RecipeIngredient> recipe1Ingredients = new ArrayList<>();
		List<RecipeIngredient> recipe2Ingredients = new ArrayList<>();
		List<RecipeIngredient> recipe3Ingredients = new ArrayList<>();

		recipe1Ingredients.add(ingredient1);
		recipe1Ingredients.add(ingredient2);
		recipe1Ingredients.add(ingredient3);

		recipe2Ingredients.add(ingredient4);
		recipe2Ingredients.add(ingredient1);

		recipe3Ingredients.add(ingredient1);
		recipe3Ingredients.add(ingredient4);
		recipe3Ingredients.add(ingredient6);
		recipe3Ingredients.add(ingredient5);

		Recipe recipe1 = new Recipe(1, "recipe 1", "1 hour", 5, recipe1Ingredients,
				"Some basic instructions with an oven", false);
		Recipe recipe2 = new Recipe(2, "recipe 2", "3 hours", 2, recipe2Ingredients,
				"Some basic instructions with a stove top", true);
		Recipe recipe3 = new Recipe(3, "recipe 3", "45mins", 3, recipe3Ingredients, "Complex fire stuffs", false);

		recipes = new ArrayList<>();
		recipes.add(recipe1);
		recipes.add(recipe2);
		recipes.add(recipe3);
	}

	@AfterEach
	public void teardown() {
		recipes.clear();
	}

	@Test
	void testGetAllRecipes() throws Exception {
		when(recipeService.getRecipes(null, null, null, null)).thenReturn(recipes);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/recipes")).andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].ingredients.length()").value(4))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("recipe 1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("recipe 2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));

	}

	@Test
	void testGetAllRecipesWithIsVegetarianTrue() throws Exception {
		List<Recipe> vegetarianRecipes = new ArrayList<>();
		vegetarianRecipes.add(recipes.get(1));

		when(recipeService.getRecipes(true, null, null, null)).thenReturn(vegetarianRecipes);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/recipes?isVegetarian=true")).andDo(print())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("recipe 2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2));
	}

	@Test
	void testGetAllRecipesWithIncludeExcludeIngredient() throws Exception {
		when(recipeService.getRecipes(null, "Milk", "Grapes", null)).thenReturn(recipes);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/recipes?includesIngredient=Milk&excludesIngredient=Grapes"))
				.andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].ingredients.length()").value(4))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("recipe 1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("recipe 2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));

	}

	@Test
	void testGetAllRecipesWithInstructionMatch() throws Exception {
		List<Recipe> matchingRecipes = new ArrayList<>();
		matchingRecipes.add(recipes.get(2));

		when(recipeService.getRecipes(null, null, null, "Fire")).thenReturn(matchingRecipes);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/recipes?instructionContains=Fire")).andDo(print())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("recipe 3"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(3));

	}

	@Test
	void testAddRecipe() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/recipes").contentType("application/json")
				.content(objectMapper.writeValueAsString(recipes.get(0)))).andExpect(status().isCreated());

	}

	@Test
	void testGetRecipeById() throws Exception {
		when(recipeService.getRecipe(1)).thenReturn(recipes.get(0));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("recipe 1"));

	}

	@Test
	void testUpdateRecipe() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.put("/recipes/1").contentType("application/json")
				.content(objectMapper.writeValueAsString(recipes.get(0)))).andExpect(status().isNoContent());
	}

	@Test
	void testDeleteRecipe() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1")).andExpect(status().isNoContent());
	}

	@Test
	void testGetRecipeServings() throws Exception {
		when(recipeService.getServingsForRecipe(1)).thenReturn(recipes.get(0).getPeopleServed());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1/servings")).andDo(print())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value(recipes.get(0).getPeopleServed()));

	}

	@Test
	void testGetIngredientsForRecipe() throws Exception {
		when(recipeService.getRecipeIngredients(1)).thenReturn(recipes.get(0).getIngredients());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1/ingredients")).andDo(print())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$").exists()).andExpect(
						MockMvcResultMatchers.jsonPath("$.length()").value(recipes.get(0).getIngredients().size()));

	}

	@Test
	void testUpdateRecipeIngredient() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/recipes/1/ingredients/1").contentType("application/json")
						.content(objectMapper.writeValueAsString(recipes.get(0).getIngredients().get(1))))
				.andExpect(status().isNoContent());
	}

	@Test
	void testDeleteRecipeIngredient() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1/ingredients/1")).andExpect(status().isNoContent());
	}

	@Test
	void testAddRecipeIngredient() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/recipes/1/ingredients").contentType("application/json")
						.content(objectMapper.writeValueAsString(recipes.get(0).getIngredients().get(1))))
				.andExpect(status().isCreated());

	}

}
