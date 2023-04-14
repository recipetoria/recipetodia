package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.entity.NewRecipeRequest;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.entity.RecipeResponse;
import com.jit.rec.recipetoria.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeResponse> getAllRecipes(){
        List<Recipe>allRecipes = recipeRepository.findAll();
        List<RecipeResponse> recipeResponses = new ArrayList<>();
        for (Recipe r : allRecipes){
            recipeResponses.add(newRecipeRequestMapper(r));
        }
        return recipeResponses;
    }

    public void createNewRecipe(NewRecipeRequest newRecipeRequest){
        Recipe recipe = new Recipe();

        if(newRecipeRequest.getName()!=null){
            recipe.setName(newRecipeRequest.getName());
            recipe.setInstructions(newRecipeRequest.getInstructions());
            recipe.setLinks(newRecipeRequest.getLinks());
            recipe.setTags(newRecipeRequest.getTags());
            recipe.setIngredientList(newRecipeRequest.getIngredients());
            recipe.setInstructionPhotos(newRecipeRequest.getInstructionPhotos());
            recipe.setMainPhoto(newRecipeRequest.getMainPhoto());
            recipeRepository.save(recipe);
        }
    }
    public void deleteRecipeById(Long id){
        recipeRepository.deleteById(id);
    }

    public Recipe getRecipeById(Long id) {
       return recipeRepository.findById(id).orElseThrow(() -> new IllegalStateException("NOT FOUND"));
    }

    public RecipeResponse newRecipeRequestMapper (Recipe recipe){
        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setName(recipe.getName());
        recipeResponse.setTags(recipe.getRecipeTagNames());
        recipeResponse.setMainPhoto(recipe.getMainPhoto());
        recipeResponse.setInstructions(recipe.getInstructions());
        recipeResponse.setIngredients(recipe.getIngredientList());
        recipeResponse.setInstructionPhotos((recipe.getInstructionPhotos()));

        return recipeResponse;
    }
}

