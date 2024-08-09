package de.cooky.service;

import de.cooky.data.IngredientToRecipePart;
import de.cooky.data.SaveStatistics;
import de.cooky.data.ShoppingItem;
import de.cooky.repository.IngredientToRecipePartRepository;
import de.cooky.repository.ShoppingItemRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingItemService {

	@Autowired
	private ShoppingItemRepository shoppingItemRepo;

	@Autowired
	private IngredientToRecipePartRepository ingrRepo;

	public Pair<Integer, Integer> enhanceShoppingListByIngredients(List<Long> ingredientIds) {

		List<IngredientToRecipePart> ingredients = ingrRepo.findAllById(ingredientIds);
		Set<String> ingrNames = ingredients.stream().map(ingr -> ingr.getIngredient().getName()).collect(Collectors.toSet());

		Map<String, List<IngredientToRecipePart>> ingredientNames = new HashMap<>();
		for (IngredientToRecipePart itr : ingredients) {
			List<IngredientToRecipePart> list = ingredientNames.computeIfAbsent(itr.getIngredient().getName(), k -> new ArrayList<>());
			list.add(itr);
		}

		List<ShoppingItem> items = shoppingItemRepo.findByNameIn(ingrNames);

		int countOfNewItems = 0;
		int countOfUpdatedItems = 0;

		for(Map.Entry<String, List<IngredientToRecipePart>> entry : ingredientNames.entrySet()){

			String ingredientName = entry.getKey();

			ShoppingItem shoppingItem = getShoppingItemByIngredientName(items, ingredientName);

			for (IngredientToRecipePart itrp : entry.getValue()) {
				if(shoppingItem.getAmount() == null)
				{
					shoppingItem.setAmount(itrp.getAmount());
				}else if(itrp.getAmount() != null){
					shoppingItem.setAmount(shoppingItem.getAmount() + itrp.getAmount());
				}
				shoppingItem.setUnit(itrp.getUnit());
			}
			if(shoppingItem.getId() == null){
				countOfNewItems++;
			}else{
				countOfUpdatedItems++;
			}
		}

		updateOrderAndSaveAll(items);

		return Pair.of(countOfNewItems, countOfUpdatedItems);
	}

	private static ShoppingItem getShoppingItemByIngredientName(List<ShoppingItem> items, String ingredientName) {
		ShoppingItem shoppingItem = items.stream().filter(item -> item.getName().equals(ingredientName)).findFirst().orElse(null);
		if(shoppingItem == null){
			shoppingItem = new ShoppingItem();
			shoppingItem.setName(ingredientName);
			items.add(shoppingItem);
		}
		return shoppingItem;
	}

	/**
	 * This is faulty as we updae only a subset of the shopping items. But shopping items are way more there
	 */
	public SaveStatistics<ShoppingItem> updateOrderAndSaveAll(List<ShoppingItem> list) {

		long start = System.currentTimeMillis();

		int order = 0;
		for (ShoppingItem shoppingItem : list) {
			shoppingItem.setItemOrder(order++);
		}

		list.removeIf(item -> StringUtils.isEmpty(item.getName()));

		List<ShoppingItem> entities = shoppingItemRepo.saveAll(list);

		long end = System.currentTimeMillis();

		return new SaveStatistics<>(entities, end - start);
    }
}
