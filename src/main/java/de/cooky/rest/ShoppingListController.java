package de.cooky.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.cooky.data.ShoppingItem;
import de.cooky.repository.ShoppingItemRepository;

@RestController
@RequestMapping("/rest/shoppinglist")
public class ShoppingListController {

	@Autowired
	private ShoppingItemRepository shoppingListRepo;

	@GetMapping
	public List<ShoppingItem> get() {

		return shoppingListRepo.findAll();
	}

	@PostMapping
	public List<ShoppingItem> save(@RequestBody List<ShoppingItem> list) {

        return shoppingListRepo.saveAll(list);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable long id) {
		shoppingListRepo.deleteById(id);
	}
}
