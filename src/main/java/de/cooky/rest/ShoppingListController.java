package de.cooky.rest;

import de.cooky.data.SaveStatistics;
import de.cooky.data.ShoppingItem;
import de.cooky.repository.RecipeRepository;
import de.cooky.repository.ShoppingItemRepository;
import de.cooky.service.ShoppingItemService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/shoppinglist")
public class ShoppingListController {

	@Autowired
	private ShoppingItemRepository shoppingListRepo;

	@Autowired
	private ShoppingItemService shoppingItemService;

	@Autowired
	private RecipeRepository recipeRepo;

	@GetMapping
	public List<ShoppingItem> get() {
		return shoppingListRepo.findAllByOrderByItemOrderAsc();
	}

	@PutMapping("/enhance")
	@Deprecated
	public Map<String, Object> enhance(@RequestBody List<Long> ingredientToRecipeIds) {
		Pair<Integer, Integer> countOfNewAndUpdatedItems = shoppingItemService.enhanceShoppingListByIngredients(ingredientToRecipeIds);

		Map<String, Object> result = new HashMap<>();
		result.put("new", countOfNewAndUpdatedItems.getLeft());
		result.put("updated", countOfNewAndUpdatedItems.getRight());

		return result;
	}

	@PostMapping
	public SaveStatistics<ShoppingItem> save(@RequestBody List<ShoppingItem> list) {
		return shoppingItemService.updateOrderAndSaveAll(list);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable long id) {
		shoppingListRepo.deleteById(id);
	}

	@DeleteMapping
	public void delete() {
		shoppingListRepo.deleteAll();
	}
}
