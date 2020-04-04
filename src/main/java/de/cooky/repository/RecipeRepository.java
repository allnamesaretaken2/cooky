package de.cooky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.cooky.data.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	List<Recipe> findByNameContainingIgnoreCase(String name);
}
