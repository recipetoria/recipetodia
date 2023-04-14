package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.RecipeDTO;
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
    public List<RecipeDTO> getAllRecipes(){
        return recipeService.getAllRecipes();
    }

    @PostMapping
    public void createNewRecipe(@RequestBody RecipeDTO recipeDTO){
        recipeService.createNewRecipe(recipeDTO);
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
