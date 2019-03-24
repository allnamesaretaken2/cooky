package de.cooky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.cooky.data.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

	Ingredient getByName(String name);

}
