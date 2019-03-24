package de.cooky;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.cooky.repository.IngredientRepository;
import de.cooky.repository.RecipeRepository;

@ComponentScan(basePackages= {"de.cooky"})
@RestController
@EnableAutoConfiguration
@RequestMapping("/hugo2")
public class CookyApp {

	@Autowired
	private IngredientRepository repository;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private EntityManager em;
	
	@RequestMapping("/hugo")
	String home() {
		
		return "ingredient: ";
	}

	public static void main(String[] args) {
		SpringApplication.run(CookyApp.class, args);
	}

}