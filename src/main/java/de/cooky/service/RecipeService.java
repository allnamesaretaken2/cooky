package de.cooky.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

	public Recipe save(Recipe recipe) {

		recipe.getIngredients().forEach(ingr -> {

			Ingredient ingredient = ingr.getIngredient();

			if (ingredient == null) {
				throw new CookyErrorMsg("an ingredient is missing");
			}
			;

			if (StringUtils.isEmpty(ingredient.getName())) {
				throw new CookyErrorMsg("an ingredient has no name");
			}

			Ingredient ingredientFromDB = ingredientService.getOrCreateIngredient(ingredient.getName());
			ingr.setIngredient(ingredientFromDB);
		});

		return recipeRepo.save(recipe);

	}
}
