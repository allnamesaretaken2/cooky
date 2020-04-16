package de.cooky.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cooky.data.Ingredient;
import de.cooky.data.IngredientToRecipe;
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

	@Autowired
	private ShoppingItemService shoppingItemService;

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

	public void setSelection(Map<Long, Boolean> newSelectionSettings) {

		Set<Long> recipeIds = newSelectionSettings.keySet();

		if (newSelectionSettings == null || newSelectionSettings.isEmpty()) {
			throw new CookyErrorMsg("cannot set the selection-flag of recipes. No Ids were given.");
		}

		List<Recipe> selected = recipeRepo.findByIdIn(recipeIds);

		List<IngredientToRecipe> ingredients = new ArrayList<>();

		selected.forEach(recipe -> {
			Boolean selectionValue = newSelectionSettings.get(recipe.getId());
			recipe.setSelected(selectionValue);

			if (selectionValue.booleanValue()) {
				ingredients.addAll(recipe.getIngredients());
			}
		});

		shoppingItemService.enhanceShoppingList(ingredients);
	}
}
