package de.cooky.service;

import de.cooky.data.Ingredient;
import de.cooky.data.IngredientToRecipePart;
import de.cooky.data.Recipe;
import de.cooky.data.RecipePart;
import de.cooky.repository.RecipeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

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
		if(recipe.getRecipeParts() == null){
			return;
		}

		//also recreate the order so that it fixes gaps or other issues in the list
		for(RecipePart part : recipe.getRecipeParts()){
			if(part.getIngredients()== null){
				continue;
			}

			int counter = 0;

			Set<IngredientToRecipePart> ingredientToRecipeParts = part.getIngredients();

			ingredientToRecipeParts.removeIf( itrp-> {
				//faulty ingredient configs will be removed.
				//It is faulty if no ingredient was connected to this part or the part was never saved
				//and is missing an ingredient name
				return itrp.getIngredient() == null ||
						(StringUtils.isEmpty(itrp.getIngredient().getName()) && itrp.getId() == null);
			});

			for (IngredientToRecipePart ingr : part.getIngredients()) {
				Ingredient ingredient = ingr.getIngredient();

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
}
