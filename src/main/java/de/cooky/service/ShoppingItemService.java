package de.cooky.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.cooky.data.IngredientToRecipePart;
import de.cooky.data.ShoppingItem;
import de.cooky.repository.ShoppingItemRepository;

@Service
public class ShoppingItemService {

	@Autowired
	private ShoppingItemRepository shoppingItemRepo;

	public void enhanceShoppingList(List<IngredientToRecipePart> ingredients) {

		List<IngredientToRecipePart> tmp = new ArrayList<>(ingredients);

		Map<String, List<IngredientToRecipePart>> ingredientNames = new HashMap<>();

		for (IngredientToRecipePart itr : ingredients) {
			List<IngredientToRecipePart> list = ingredientNames.get(itr.getIngredient().getName());

			if (list == null) {
				list = new ArrayList<>();

				ingredientNames.put(itr.getIngredient().getName(), list);
			}

			list.add(itr);
		}

		List<ShoppingItem> items = shoppingItemRepo.findByNameIn(ingredientNames.keySet());

		for (ShoppingItem item : items) {

			List<IngredientToRecipePart> list = ingredientNames.get(item.getName());

			if (list == null) {
				continue;
			}

			for (IngredientToRecipePart itr : list) {

				if (!StringUtils.equals(itr.getUnit(), item.getUnit())) {
					continue;
				}

				item.setAmount(item.getAmount() + itr.getAmount());
				tmp.remove(itr);
			}
		}

		List<ShoppingItem> newItems = new ArrayList<>();
		for (IngredientToRecipePart newIngredient : tmp) {

			ShoppingItem item = new ShoppingItem();
			item.setName(newIngredient.getIngredient().getName());
			item.setUnit(newIngredient.getUnit());
			item.setAmount(newIngredient.getAmount());

			newItems.add(item);
		}

		shoppingItemRepo.saveAll(newItems);
	}

    public List<ShoppingItem> updateOrderAndSaveAll(List<ShoppingItem> list) {

		int order = 0;
		for (ShoppingItem shoppingItem : list) {
			shoppingItem.setItemOrder(order++);
		}

		return shoppingItemRepo.saveAll(list);

    }
}
