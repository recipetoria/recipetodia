package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.RecipeDTO;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;

    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
    }

    public List<RecipeDTO> getAllRecipes(){
        List<Recipe>allRecipes = recipeRepository.findAll();
        List<RecipeDTO> recipeResponses = new ArrayList<>();
        for (Recipe r : allRecipes){
            recipeResponses.add(newRecipeRequestMapper(r));
        }
        return recipeResponses;
    }

    public void createNewRecipe(RecipeDTO recipeDTO){
        Recipe recipe = new Recipe();

        if(recipeDTO.getName()!=null){
            recipe.setName(recipeDTO.getName());
            recipe.setInstructions(recipeDTO.getInstructions());
            recipe.setLinks(recipeDTO.getLinks());
            recipe.setTags(recipe.getTags());
            //recipe.setIngredientList(recipeDTO.getIngredients());
            recipe.setInstructionPhotos(recipeDTO.getInstructionPhotos());
            recipe.setMainPhoto(recipeDTO.getMainPhoto());
            recipeRepository.save(recipe);
        }
    }
    public void deleteRecipeById(Long id){
        recipeRepository.deleteById(id);
    }

    public Recipe getRecipeById(Long id) {
       return recipeRepository.findById(id).orElseThrow(() -> new IllegalStateException("NOT FOUND"));
    }

    public RecipeDTO newRecipeRequestMapper (Recipe recipe){
        RecipeDTO recipeResponse = new RecipeDTO();
        recipeResponse.setName(recipe.getName());
        recipeResponse.setTags(recipe.getRecipeTagNames());
        recipeResponse.setMainPhoto(recipe.getMainPhoto());
        recipeResponse.setInstructions(recipe.getInstructions());
        //recipeResponse.setIngredients(recipe.getIngredientList());
        recipeResponse.setInstructionPhotos((recipe.getInstructionPhotos()));

        return recipeResponse;
    }

}

