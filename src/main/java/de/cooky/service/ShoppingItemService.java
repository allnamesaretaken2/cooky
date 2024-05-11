package de.cooky.service;

import de.cooky.data.*;
import de.cooky.repository.RecipeRepository;
import de.cooky.repository.RecipeToShopRepository;
import de.cooky.repository.ShoppingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShoppingItemService {

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private ShoppingItemRepository shoppingItemRepo;
	
	@Autowired
	private RecipeToShopRepository recipeToShopRepo;

	public void enhanceShoppingList(List<Long> recipeIdsAsArray) {

		List<RecipeToShop> all = recipeToShopRepo.findAll();
		List<Long> recipesWeAlreadyKnow = all.stream().map(RecipeToShop::getIdRecipe).collect(Collectors.toList());

		List<Long> recipeIds = new ArrayList<>(recipeIdsAsArray);

		//ignore those we already have in our list
		recipeIds.removeAll(recipesWeAlreadyKnow);

		List<Recipe> recipesToBeCooked = recipeRepo.findAllById(recipeIds);

		Map<String, List<IngredientToRecipePart>> ingredientNames = new HashMap<>();

		for(Recipe recipe : recipesToBeCooked) {
			for (RecipePart part : recipe.getRecipeParts()) {
				for (IngredientToRecipePart itr : part.getIngredients()) {
                    List<IngredientToRecipePart> list = ingredientNames.computeIfAbsent(itr.getIngredient().getName(), k -> new ArrayList<>());
                    list.add(itr);
				}
			}
		}

		List<ShoppingItem> items = shoppingItemRepo.findByNameIn(ingredientNames.keySet());

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
		}

		updateOrderAndSaveAll(items);
		for(Long idRecipe : recipeIds){
			RecipeToShop shop = new RecipeToShop();
			shop.setIdRecipe(idRecipe);
			recipeToShopRepo.save(shop);
		}
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

	public SaveStatistics<ShoppingItem> updateOrderAndSaveAll(List<ShoppingItem> list) {

		long start = System.currentTimeMillis();

		int order = 0;
		for (ShoppingItem shoppingItem : list) {
			shoppingItem.setItemOrder(order++);
		}

		long end = System.currentTimeMillis();

		List<ShoppingItem> entities = shoppingItemRepo.saveAll(list);

		return new SaveStatistics<>(entities, end - start);
    }
}
