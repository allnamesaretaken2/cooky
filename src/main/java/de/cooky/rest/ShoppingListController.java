package de.cooky.rest;

import de.cooky.data.Recipe;
import de.cooky.data.RecipeToShop;
import de.cooky.data.SaveStatistics;
import de.cooky.data.ShoppingItem;
import de.cooky.exceptions.CookyErrorMsg;
import de.cooky.repository.RecipeRepository;
import de.cooky.repository.RecipeToShopRepository;
import de.cooky.repository.ShoppingItemRepository;
import de.cooky.service.ShoppingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/shoppinglist")
public class ShoppingListController {

	@Autowired
	private ShoppingItemRepository shoppingListRepo;

	@Autowired
	private ShoppingItemService shoppingItemService;

	@Autowired
	private RecipeToShopRepository recipeToShopRepo;

	@Autowired
	private RecipeRepository recipeRepo;

	@GetMapping
	public List<ShoppingItem> get() {
		return shoppingListRepo.findAllByOrderByItemOrderAsc();
	}

	@PutMapping("/enhance")
	public void enhance(@RequestBody List<Long> recipeIds){
		shoppingItemService.enhanceShoppingList(recipeIds);
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
		recipeToShopRepo.deleteAll();
	}

	@GetMapping("/getRecipeNamesWeAlreadyAddedToTheShoppingList")
	public List<String> getRecipeNamesWeAlreadyAddedToTheShoppingList(){
		List<Long> recipeIds = recipeToShopRepo.findAll().stream().map(RecipeToShop::getIdRecipe).collect(Collectors.toList());

		return recipeRepo.findAllById(recipeIds).stream().map(Recipe::getName).collect(Collectors.toList());
	}
}
