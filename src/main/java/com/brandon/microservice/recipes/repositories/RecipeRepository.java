package com.brandon.microservice.recipes.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.brandon.microservice.recipes.models.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
	List<Recipe> findAllByisVegetarian(Boolean isVegetarian);
	List<Recipe> findAllByinstructionsContains(String text);
	List<Recipe> findAllByisVegetarianAndInstructionsContains(Boolean isVegetarian, String text);
}
