package de.cooky.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.cooky.data.IngredientToRecipe;
import de.cooky.data.ShoppingItem;
import de.cooky.repository.ShoppingItemRepository;

@Service
public class ShoppingItemService {

	@Autowired
	private ShoppingItemRepository shoppingItemRepo;

	public void enhanceShoppingList(List<IngredientToRecipe> ingredients) {

		List<IngredientToRecipe> tmp = new ArrayList<IngredientToRecipe>(ingredients);

		Map<String, List<IngredientToRecipe>> ingredientNames = new HashMap<>();

		for (IngredientToRecipe itr : ingredients) {
			List<IngredientToRecipe> list = ingredientNames.get(itr.getIngredient().getName());

			if (list == null) {
				list = new ArrayList<>();

				ingredientNames.put(itr.getIngredient().getName(), list);
			}

			list.add(itr);
		}

		List<ShoppingItem> items = shoppingItemRepo.findByNameIn(ingredientNames.keySet());

		for (ShoppingItem item : items) {

			List<IngredientToRecipe> list = ingredientNames.get(item.getName());

			if (list == null) {
				continue;
			}

			for (IngredientToRecipe itr : list) {

				if (!StringUtils.equals(itr.getUnit(), item.getUnit())) {
					continue;
				}

				item.setAmount(item.getAmount() + itr.getAmount());
				tmp.remove(itr);
			}
		}

		List<ShoppingItem> newItems = new ArrayList<>();
		for (IngredientToRecipe newIngredient : tmp) {

			ShoppingItem item = new ShoppingItem();
			item.setName(newIngredient.getIngredient().getName());
			item.setUnit(newIngredient.getUnit());
			item.setAmount(newIngredient.getAmount());

			newItems.add(item);
		}

		shoppingItemRepo.saveAll(newItems);
	}
}
