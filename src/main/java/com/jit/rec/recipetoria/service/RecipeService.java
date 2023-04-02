package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.entity.NewRecipeRequest;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
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
        }
        recipeRepository.save(recipe);
    }



}
