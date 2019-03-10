package de.cooky;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.cooky.data.Ingredient;
import de.cooky.data.Ingredient.UnitType;
import de.cooky.data.Recipe;
import de.cooky.repository.IngredientRepository;
import de.cooky.repository.RecipeRepository;

@RestController
@EnableAutoConfiguration
public class CookyApp {

	@Autowired
	private IngredientRepository repository;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private EntityManager em;

	@RequestMapping("/hugo")
	String home() {

		long count1 = repository.count();

		Ingredient s = new Ingredient();
		
		s.setUnit(UnitType.SPOONS);
		s.setAmount(2f);

		Recipe r = new Recipe();
		r.setName("Puffer");
		r.getIngredients().add(s);

		recipeRepository.save(r);

		em.detach(r);

		return "ingredient: " + r;
	}

	public static void main(String[] args) {
		SpringApplication.run(CookyApp.class, args);
	}

}