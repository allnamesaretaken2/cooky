package de.cooky.service;

import de.cooky.data.IngredientToRecipePart;
import de.cooky.data.Recipe;
import de.cooky.data.RecipePart;
import de.cooky.data.SelectedEntry;
import de.cooky.exceptions.CookyErrorMsg;
import de.cooky.repository.RecipeRepository;
import de.cooky.repository.SelectedEntryRepository;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class SelectedEntryService {

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private ShoppingItemService shoppingItemService;

	@Autowired
	private SelectedEntryRepository selectedEntryRepo;

	public void setSelection(LinkedHashMap<String, Boolean> newSelectionSettings) {

		if (newSelectionSettings.isEmpty()) {
			throw new CookyErrorMsg("cannot set the selection-flag of recipes. No names were given.");
		}

		List<SelectedEntry> entries = selectedEntryRepo.findAll();
		List<SelectedEntry> entriesToDelete = new ArrayList<>();

		List<IngredientToRecipePart> ingredients = new ArrayList<>();

		AtomicInteger order = new AtomicInteger(0);

		newSelectionSettings.forEach((name, selected) -> {

			SelectedEntry oldEntry = entries.stream().filter(entry -> entry.getName().equals(name)).findAny().orElse(null);
			if(oldEntry != null) {
				if(BooleanUtils.isFalse(selected)) {
					//this one is not selected anymore
					entries.remove(oldEntry);
					entriesToDelete.add(oldEntry);
				}
				//and this one is still selected and nothing has to be
				oldEntry.setSortOrder(order.getAndIncrement());
				return;
			}

			SelectedEntry newEntry = new SelectedEntry();
			newEntry.setName(name);
			newEntry.setSortOrder(order.getAndIncrement());
			entries.add(newEntry);

			if (selected) {
				Recipe recipe = recipeRepo.findByName(name);
				if(recipe == null){
					//do nothing
					return;
				}

				for (RecipePart recipePart : recipe.getRecipeParts()) {
					ingredients.addAll(recipePart.getIngredients());
				}
			}
		});

		selectedEntryRepo.saveAll(entries);
		selectedEntryRepo.deleteAll(entriesToDelete);
		shoppingItemService.enhanceShoppingList(ingredients);
	}

	public void setComment(Long id, String comment) {
		SelectedEntry entry = selectedEntryRepo.getOne(id);
		entry.setComment(comment);
	}
}
