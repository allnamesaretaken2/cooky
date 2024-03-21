package de.cooky.rest;

import de.cooky.data.Recipe;
import de.cooky.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/ingredients")
public class IngredientController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping("/insertFromString/{recipeName}")
    public ResponseEntity<Boolean> create(@RequestBody String ingredientsAsStringBlobb, @PathVariable String recipeName) {

        Recipe recipe = recipeService.insertFromString(ingredientsAsStringBlobb, recipeName);

        //TODO better response, please
        return ResponseEntity.accepted().body(true);
    }
}
