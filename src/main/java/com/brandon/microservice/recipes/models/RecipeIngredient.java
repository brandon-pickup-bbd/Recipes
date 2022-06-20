package com.brandon.microservice.recipes.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(description = "Ingredients in specific relation to a recipe")
public class RecipeIngredient {

	@Id
	@GeneratedValue
	private Integer id;

	private String description;

	private String ingredientQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Recipe recipe;

	protected RecipeIngredient() {
	}

	public RecipeIngredient(Integer id, String description, String ingredientQuantity, Recipe recipe) {
		super();
		this.id = id;
		this.description = description;
		this.ingredientQuantity = ingredientQuantity;
		this.recipe = recipe;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIngredientQuantity() {
		return ingredientQuantity;
	}

	public void setIngredientQuantity(String ingredientQuantity) {
		this.ingredientQuantity = ingredientQuantity;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public String toString() {
		return "RecipeIngredient [id=" + id + ", Description=" + description + ", IngredientQuantity="
				+ ingredientQuantity + "]";
	}

}
