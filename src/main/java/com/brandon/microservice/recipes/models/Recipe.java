package com.brandon.microservice.recipes.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details about the recipes.")
@Entity
public class Recipe {

	@Id
	@GeneratedValue
	private Integer id;

	@NotNull(message = "Description is required")
	@ApiModelProperty(notes = "Description is required")
	private String description;

	@NotNull(message = "PreparationTime is required")
	@ApiModelProperty(notes = "PreparationTime is required")
	private String preparationTime;

	@Min(value = 1, message = "PeopleServed must be >= 1")
	@ApiModelProperty(notes = "PeopleServed is required and must be >= 1")
	private Integer peopleServed;

	@Size(min = 1, message = "Atleast 1 ingredient is required")
	@ApiModelProperty(notes = "Atleast 1 ingredient is required")
	@OneToMany(mappedBy = "recipe")
	private List<RecipeIngredient> ingredients;

	@NotNull(message = "Instructions are required")
	@ApiModelProperty(notes = "Instructions are required")
	private String instructions;
	
	private Boolean isVegetarian;

	protected Recipe() {
	}

	public Recipe(Integer id, @NotNull(message = "Description is required") String description,
			@NotNull(message = "PreparationTime is required") String preparationTime,
			@Min(value = 1, message = "PeopleServed must be >= 1") Integer peopleServed,
			@Size(min = 1, message = "Atleast 1 ingredient is required") List<RecipeIngredient> ingredients,
			@NotNull(message = "Instructions are required") String instructions,
			Boolean isVegetarian) {
		super();
		this.id = id;
		this.description = description;
		this.preparationTime = preparationTime;
		this.peopleServed = peopleServed;
		this.ingredients = ingredients;
		this.instructions = instructions;
		this.isVegetarian = isVegetarian;
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

	public String getPreparationTime() {
		return preparationTime;
	}

	public void setPreparationTime(String preparationTime) {
		this.preparationTime = preparationTime;
	}

	public Integer getPeopleServed() {
		return peopleServed;
	}

	public void setPeopleServed(Integer peopleServed) {
		this.peopleServed = peopleServed;
	}

	public List<RecipeIngredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<RecipeIngredient> ingredients) {
		this.ingredients = ingredients;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	public Boolean getIsVegetarian() {
		return isVegetarian;
	}

	public void setIsVegetarian(Boolean isVegetarian) {
		this.isVegetarian = isVegetarian;
	}

}
