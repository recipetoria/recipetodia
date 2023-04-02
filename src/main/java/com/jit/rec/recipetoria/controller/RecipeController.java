package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.entity.NewRecipeRequest;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public List<Recipe> getAllRecipes(){
        return recipeService.getAllRecipes();
    }

    @PostMapping
    public void createNewRecipe(@RequestBody NewRecipeRequest newRecipeRequest){
        recipeService.createNewRecipe(newRecipeRequest);
    }

}
