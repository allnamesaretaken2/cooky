package de.cooky.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import de.cooky.data.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	List<Recipe> findByNameContainingIgnoreCase(String name);
	
	List<Recipe> findBySelected(boolean selected);

	List<Recipe> findByIdIn(Set<Long> recipeIds);
}
