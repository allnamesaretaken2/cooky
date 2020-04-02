package de.cooky.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.cooky.data.Recipe;
import de.cooky.repository.RecipeRepository;
import de.cooky.service.ChefkochImportService;
import de.cooky.service.RecipeService;

@RestController
@RequestMapping("/rest/recipes")
public class RecipeController {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeController.class);
	
	@Autowired
	private RecipeService recipeService; 
	
	@Autowired
	private RecipeRepository recipeRepo;
	
	@Autowired
	private ChefkochImportService chefkochImportService;

	@GetMapping
	public List<Recipe> getAll() {

		return recipeRepo.findAll();
	}

	@GetMapping("{id}")
	public Recipe getOne(@PathVariable long id) {

		Recipe recipe = recipeRepo.findById(id).get();
		
		return recipe;
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable long id) {
		recipeRepo.deleteById(id);
	}

	@PostMapping
	public ResponseEntity<Recipe> create(@RequestBody Recipe recipe) {

		Recipe saved = recipeService.create(recipe);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId())
				.toUri();

		return ResponseEntity.created(location).body(saved);
	}

	@PutMapping("{id}")
	public ResponseEntity<Object> update(@RequestBody Recipe recipe, @PathVariable long id) {

		Recipe updated = recipeService.update(recipe, id);

		return ResponseEntity.accepted().body(updated);
	}

	@PostMapping("/importFromChefkoch")
	public ResponseEntity<Recipe> importFromChefkoch(@RequestBody Import importObject) {

		Recipe result = chefkochImportService.importRecipe(importObject.url);
		
		return ResponseEntity.ok(result);
	}
	
	static class Import{
		
		String url;
		
		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getUrl() {
			return url;
		}
		
	}
}
