package de.cooky.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

@RestController
@RequestMapping("/rest/recipes/")
public class RecipeController {

	@Autowired
	private RecipeRepository recipeRepo;

	@GetMapping("")
	public List<Recipe> getAll() {

		return recipeRepo.findAll();
	}

	@GetMapping("{id}")
	public Recipe getOne(@PathVariable long id) {

		return recipeRepo.getOne(id);
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable long id) {
		recipeRepo.deleteById(id);
	}

	@PostMapping("")
	public ResponseEntity<Object> create(@RequestBody Recipe recipe) {

		Recipe saved = recipeRepo.save(recipe);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping("{id}")
	public ResponseEntity<Object> update(@RequestBody Recipe recipe, @PathVariable long id) {

		Optional<Recipe> optional = recipeRepo.findById(id);

		if (!optional.isPresent())
			return ResponseEntity.notFound().build();

		recipe.setId(id);

		recipeRepo.save(recipe);

		return ResponseEntity.noContent().build();
	}
}
