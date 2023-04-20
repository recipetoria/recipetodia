package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class RecipeDTO {
    @Nullable
    private Long id;

    @NotNull(message = "name should not be null")
    private String name;

    @Nullable
    private Long applicationUserId;

    @Nullable
    private String mainPhoto;

    @Nullable
    private List<String> tags;

    @Nullable
    private List<IngredientDTO> ingredients;

    @Nullable
    private List<String> instructions;

    @Nullable
    private List<String> instructionPhotos;

    @Nullable
    private List<String> links;

    public List<IngredientDTO> getIngredients(){
        if (this.ingredients == null){
            return this.ingredients = new ArrayList<>();
        }
        else
            return this.ingredients;
    }

    public static RecipeDTO convertToDTO (Recipe recipe){
        RecipeDTO recipeResponse = new RecipeDTO();
        //chatGPT =)
        recipeResponse.setApplicationUserId(
                Optional.ofNullable(recipe.getApplicationUser())
                .map(ApplicationUser::getId)
                .orElse(null)
        );
        recipeResponse.setId(recipe.getId());
        recipeResponse.setName(recipe.getName());
        recipeResponse.setTags(recipe.getRecipeTagNames());
        recipeResponse.setMainPhoto(recipe.getMainPhoto());
        recipeResponse.setInstructions(recipe.getInstructions());
        recipeResponse.setInstructionPhotos((recipe.getInstructionPhotos()));
        recipeResponse.setLinks(recipe.getLinks());
        for (Ingredient ingredientFromRecipe : recipe.getIngredientList()) {
            recipeResponse.getIngredients().add(IngredientDTO.convertToDTO(ingredientFromRecipe));
        }
        return recipeResponse;
    }
}
