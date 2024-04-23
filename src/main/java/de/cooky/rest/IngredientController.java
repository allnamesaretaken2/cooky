package de.cooky.rest;

import de.cooky.data.Ingredient;
import de.cooky.data.IngredientToRecipePart;
import de.cooky.data.Recipe;
import de.cooky.data.RecipePart;
import de.cooky.repository.IngredientRepository;
import de.cooky.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/rest/ingredients")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepo;

    @Autowired
    private RecipeRepository recipeRepo;

    @GetMapping("getByRecipe/{recipeName}")
    public Set<IngredientToRecipePart> getByRecipe(@PathVariable String recipeName){

        Recipe recipe = recipeRepo.findByName(recipeName);

        Set<IngredientToRecipePart> result = new LinkedHashSet<>();

        for (RecipePart recipePart : recipe.getRecipeParts()) {
            result.addAll(recipePart.getIngredients());
        }

        return result;
    }

    @GetMapping
    public List<Ingredient> get(){
        return ingredientRepo.findAll(Sort.by(Sort.Order.asc("name")));
    }
}
