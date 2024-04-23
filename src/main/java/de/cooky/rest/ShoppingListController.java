package de.cooky.rest;

import de.cooky.data.ShoppingItem;
import de.cooky.repository.RecipeToShopRepository;
import de.cooky.repository.ShoppingItemRepository;
import de.cooky.service.ShoppingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/shoppinglist")
public class ShoppingListController {

	@Autowired
	private ShoppingItemRepository shoppingListRepo;

	@Autowired
	private ShoppingItemService shoppingItemService;

	@Autowired
	private RecipeToShopRepository recipeToShopRepo;

	@GetMapping
	public List<ShoppingItem> get() {

		return shoppingListRepo.findAllByOrderByItemOrderAsc();
	}

	@PutMapping("/enhance")
	public void enhance(@RequestBody List<Long> recipeIds){
		shoppingItemService.enhanceShoppingList(recipeIds);
	}

	@PostMapping
	public List<ShoppingItem> save(@RequestBody List<ShoppingItem> list) {

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
}
