package de.cooky.rest;

import de.cooky.data.Recipe;
import de.cooky.repository.RecipeRepository;
import de.cooky.service.ChefkochImportService;
import de.cooky.service.RecipeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/recipes")
public class RecipeController {

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private ChefkochImportService chefkochImportService;

	@GetMapping()
	public List<Recipe> getAll(@RequestParam(required = false) String name) {

		if (StringUtils.isEmpty(name)) {
			return recipeRepo.findAll();
		} else {
			return recipeRepo.findByNameContainingIgnoreCase(name);
		}
	}

	@GetMapping("/selected")
	public List<Recipe> getSelected() {
		return recipeRepo.findBySelected(true);
	}

	@PutMapping("/select")
	public void Selected(@RequestBody Map<Long, Boolean> selectionSettings) {

		recipeService.setSelection(selectionSettings);
	}

	@GetMapping("{id}")
	public Recipe getOne(@PathVariable long id) {

		return recipeRepo.findById(id).get();
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

		return ResponseEntity.ok(saved);
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

	static class Import {

		String url;

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}
	}
}
