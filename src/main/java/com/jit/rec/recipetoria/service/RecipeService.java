package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.dto.RecipeDTO;
import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.repository.RecipeRepository;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        for (Recipe recipe : allRecipes){
            recipeResponses.add(RecipeDTO.convertToDTO(recipe));
        }
        return recipeResponses;
    }

    public RecipeDTO createNewRecipe(RecipeDTO recipeDTO){
        //todo: adjust tag saving from ids or from tag names?
        Recipe recipe = new Recipe();

        if(recipeDTO.getName()!=null){
            recipe.setName(recipeDTO.getName());

            Optional.ofNullable(recipeDTO.getInstructions())
                    .ifPresent(recipe::setInstructions);

            Optional.ofNullable(recipeDTO.getLinks())
                    .ifPresent(recipe::setLinks);

//todo:  adjust tag logic

            if (recipe.getTags() != null) {
                recipe.setTags(recipe.getTags());
            }

            Optional.ofNullable(recipeDTO.getInstructionPhotos())
                    .ifPresent(recipe::setInstructionPhotos);

            Optional.ofNullable(recipeDTO.getMainPhoto())
                    .ifPresent(recipe::setMainPhoto);

            Optional.ofNullable(recipeDTO.getInstructions())
                    .ifPresent(recipe::setInstructions);

            Optional.ofNullable(recipeDTO.getInstructionPhotos())
                    .ifPresent(recipe::setInstructionPhotos);

            Optional.ofNullable(recipeDTO.getLinks())
                    .ifPresent(recipe::setLinks);

            Optional.ofNullable(recipeDTO.getIngredients())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(ingredientService::dtoToIngredientConverter)
                    .forEach(recipe.getIngredientList()::add);

//            if (recipeDTO.getIngredients() != null) {
//                for(IngredientDTO newIngredientDTO : recipeDTO.getIngredients()){
//                    Ingredient newIngredient = ingredientService.dtoToIngredientConverter(newIngredientDTO);
//                    recipe.getIngredientList().add(newIngredient);
//                }
//            }


            recipe.setApplicationUser((ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            recipeRepository.save(recipe);
        }
        return RecipeDTO.convertToDTO(recipe);
    }

    public RecipeDTO updateRecipeById(Long recipeToUpdateId, RecipeDTO updatedRecipeDTO){
        Recipe recipeToBeUpdated = recipeRepository.findById(recipeToUpdateId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe with ID " + recipeToUpdateId + " not found"));

        if(updatedRecipeDTO.getName()!=null){
            recipeToBeUpdated.setName(updatedRecipeDTO.getName());

            Optional.ofNullable(updatedRecipeDTO.getMainPhoto())
                    .ifPresent(recipeToBeUpdated::setMainPhoto);

//todo:  adjust tag updating process

//            if (updatedRecipeDTO.getTags() != null){
//                recipeToBeUpdated.setTags();
//            }

            Optional.ofNullable(updatedRecipeDTO.getInstructions())
                    .ifPresent(recipeToBeUpdated::setInstructions);

            Optional.ofNullable(updatedRecipeDTO.getInstructionPhotos())
                    .ifPresent(recipeToBeUpdated::setInstructionPhotos);

            Optional.ofNullable(updatedRecipeDTO.getLinks())
                    .ifPresent(recipeToBeUpdated::setLinks);

//todo: turn into Optional
            if (updatedRecipeDTO.getIngredients() != null) {
                List<Ingredient> newIngredients = new ArrayList<>();
                for(IngredientDTO newIngredientDTO : updatedRecipeDTO.getIngredients()){
                    Ingredient ingredient = ingredientService.dtoToIngredientConverter(newIngredientDTO);
                    newIngredients.add(ingredient);
                }
                recipeToBeUpdated.setIngredientList(newIngredients);
            }
        }
        return RecipeDTO.convertToDTO(recipeRepository.save(recipeToBeUpdated));
    }

    public void deleteRecipeById(Long id){
        recipeRepository.deleteById(id);
    }

    public RecipeDTO getRecipeById(Long id) {
       return RecipeDTO.convertToDTO(recipeRepository.findById(id).orElseThrow(() -> new IllegalStateException("NOT FOUND")));
    }


}

