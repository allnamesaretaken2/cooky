package de.cooky.service;

import de.cooky.data.Ingredient;
import de.cooky.data.IngredientToRecipePart;
import de.cooky.data.Recipe;
import de.cooky.data.RecipePart;
import de.cooky.exceptions.CookyErrorMsg;
import de.cooky.repository.RecipeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		if(recipe.getRecipeParts() == null){
			return;
		}

		//also recreate the order so that it fixes gaps or other issues in the list
		for(RecipePart part : recipe.getRecipeParts()){
			if(part.getIngredients()== null){
				continue;
			}

			int counter = 0;

			for (IngredientToRecipePart ingr : part.getIngredients()) {
				Ingredient ingredient = ingr.getIngredient();

				if (ingredient == null) {
					throw new CookyErrorMsg("No ingredient found on ingredient_usage-object");
				}

				if (StringUtils.isEmpty(ingredient.getName())) {
					throw new CookyErrorMsg("an ingredient has no name");
				}

				Ingredient ingredientFromDB = ingredientService.getOrCreateIngredient(ingredient.getName());
				ingr.setIngredient(ingredientFromDB);
				ingr.setOrder(counter++);
			}
		}
	}

	public Recipe update(Recipe recipe, long id) {

		Long recipePresent = recipeRepo.countById(id);

		if (recipePresent != 1) {
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

		if (newSelectionSettings.isEmpty()) {
			throw new CookyErrorMsg("cannot set the selection-flag of recipes. No Ids were given.");
		}

		List<Recipe> selected = recipeRepo.findByIdIn(recipeIds);

		List<IngredientToRecipePart> ingredients = new ArrayList<>();

		selected.forEach(recipe -> {
			Boolean selectionValue = newSelectionSettings.get(recipe.getId());
			if(recipe.getSelected() && selectionValue){
				//already selected. We don't want to cook the same stuff twice
				return;
			}
			recipe.setSelected(selectionValue);

			if (selectionValue) {
				for (RecipePart recipePart : recipe.getRecipeParts()) {
					ingredients.addAll(recipePart.getIngredients());
				}
			}
		});

		shoppingItemService.enhanceShoppingList(ingredients);
	}
}
