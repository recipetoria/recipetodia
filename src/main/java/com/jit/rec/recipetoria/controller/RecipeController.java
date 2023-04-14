package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.entity.NewRecipeRequest;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.entity.RecipeResponse;
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
    public List<RecipeResponse> getAllRecipes(){
        return recipeService.getAllRecipes();
    }

    @PostMapping
    public void createNewRecipe(@RequestBody NewRecipeRequest newRecipeRequest){
        recipeService.createNewRecipe(newRecipeRequest);
    }
    @DeleteMapping("/{recipeId}")
    public void deleteRecipe(@PathVariable("recipeId") Long id){
        recipeService.deleteRecipeById(id);
    }
    @GetMapping("{recipeId}")
    public void getRecipeById(@RequestParam("recipeId") Long id){
        recipeService.getRecipeById(id);
    }





}
