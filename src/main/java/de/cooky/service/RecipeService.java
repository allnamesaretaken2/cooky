package de.cooky.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cooky.data.Ingredient;
import de.cooky.data.Recipe;
import de.cooky.exceptions.CookyErrorMsg;
import de.cooky.repository.RecipeRepository;

@Service
@Transactional
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private IngredientService ingredientService;

	public Recipe create(Recipe recipe) {

		updateIngredients(recipe);

		return recipeRepo.save(recipe);
	}

	private void updateIngredients(Recipe recipe) {

		recipe.getIngredients().forEach(ingr -> {

			Ingredient ingredient = ingr.getIngredient();

			if (ingredient == null) {
				throw new CookyErrorMsg("No ingredient found on ingredient_usage-object");
			}

			if (StringUtils.isEmpty(ingredient.getName())) {
				throw new CookyErrorMsg("an ingredient has no name");
			}

			Ingredient ingredientFromDB = ingredientService.getOrCreateIngredient(ingredient.getName());
			ingr.setIngredient(ingredientFromDB);
		});
	}

	public Recipe update(Recipe recipe, long id) {

		Optional<Recipe> optional = recipeRepo.findById(id);

		if (!optional.isPresent()) {
			throw new EntityNotFoundException("Cannot update recipe. There is none in the database with id " + id);
		}

		// if not already set but who knows
		recipe.setId(id);

		updateIngredients(recipe);

		recipe = recipeRepo.save(recipe);
		
		return recipe;

	}
}
