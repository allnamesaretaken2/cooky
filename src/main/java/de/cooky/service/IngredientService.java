package de.cooky.service;

import de.cooky.data.Ingredient;
import de.cooky.data.IngredientToRecipe;
import de.cooky.exceptions.CookyErrorMsg;
import de.cooky.repository.IngredientRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Transactional
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

	/**
	 * Creates a new set of ingredientToRecipe-Objects containing the new ingredients.
	 */
    public Set<IngredientToRecipe> createIngredientsFromString(String ingredientsAsStringBlobb) {

		String[] ingredientsAsString = ingredientsAsStringBlobb.split(";");

		Set<IngredientToRecipe> result = new LinkedHashSet<>();

		int counter = 0;
		for (String ingAsString : ingredientsAsString) {

			ingAsString = ingAsString.trim();

			String[] ingredientParts = ingAsString.split(" ");

			String ingredientName = null;
			Float amount = null;
			String unit = null;

			if(ingredientParts.length == 1){
				//only the ingredient name is given
				ingredientName = ingredientParts[0];
			}

			if(ingredientParts.length == 2){
				// the name and the number are given (e.g. 3 eggs)
				String amountAsString = ingredientParts[0];
				if(!NumberUtils.isCreatable(amountAsString)){
					throw new CookyErrorMsg("Menge der Zutaten müssen 'ne 'Zahl sein.");
				}

				ingredientName = ingredientParts[1];
				amount = Float.parseFloat(amountAsString);
			}

			if(ingredientParts.length == 3){
				// the name, the unit and the amount are given (e.g. 1 liter milk)
				String amountAsString = ingredientParts[0];
				if(!NumberUtils.isCreatable(amountAsString)){
					throw new CookyErrorMsg("Menge der Zutaten müssen 'ne 'Zahl sein.");
				}

				ingredientName = ingredientParts[2];
				unit = ingredientParts[1];
				amount = Float.parseFloat(amountAsString);
			}

			Ingredient ing = getOrCreateIngredient(ingredientName);

			IngredientToRecipe ingToRecipe = new IngredientToRecipe();
			ingToRecipe.setIngredient(ing);
			ingToRecipe.setAmount(amount);
			ingToRecipe.setUnit(unit);
			ingToRecipe.setOrder(counter++);

			result.add(ingToRecipe);
		}

		return result;
	}
}
