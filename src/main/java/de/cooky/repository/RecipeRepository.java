package de.cooky.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import de.cooky.data.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	Recipe findByName(String name);

	Long countById(Long id);

	List<Recipe> findByNameContainingIgnoreCase(String name);
}
