package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.entity.Recipe;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record RecipeShortInfoDTO(
        @Nullable
        Long id,

        @NotNull(message = "{validation.recipeDTO.name.NotNull}")
        String name,


        @Nullable
        String mainPhoto
) {
    public static RecipeShortInfoDTO convertToRecipeShortInfoDTO(Recipe recipe) {
        return new RecipeShortInfoDTO(recipe.getId(),
                recipe.getName(),
                recipe.getMainPhoto());
    }

}
