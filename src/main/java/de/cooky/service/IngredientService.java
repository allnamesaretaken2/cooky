package de.cooky.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.cooky.data.Ingredient;
import de.cooky.repository.IngredientRepository;

@Service
public class IngredientService {

	@Autowired
	private IngredientRepository ingredientRepo;

	/**
	 * get an ingredient by the name or create a new one
	 */
	public Ingredient getOrCreateTemporaryIngredient(String ingredientName) {

		Ingredient ing = ingredientRepo.getByName(ingredientName);
		
		if (ing == null) {

			ing = new Ingredient();
			ing.setName(ingredientName);
		}

		return ing;
	}
	
	public Ingredient getOrCreateIngredient(String ingredientName) {

		Ingredient ing = ingredientRepo.getByName(ingredientName);
		
		if (ing == null) {

			ing = new Ingredient();
			ing.setName(ingredientName);
			
			ing = ingredientRepo.save(ing);
		}

		return ing;
	}

}
