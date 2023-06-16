package com.jit.rec.recipetoria.recipe;

import com.jit.rec.recipetoria.tag.TagDTO;
import com.jit.rec.recipetoria.ingredient.IngredientDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.*;

public record RecipeDTO(

        @Nullable
        Long id,

        @NotNull(message = "{validation.recipeDTO.name.NotNull}")
        String name,

        @Nullable
        String mainPhoto,

        @Nullable
        Long applicationUserId,

        @Nullable
        List<TagDTO> tagDTOs,

        @Nullable
        @Valid
        List<IngredientDTO> ingredientDTOs,

        @Nullable
        List<String> instructions,

        @Nullable
        List<String> instructionPhotos,

        @Nullable
        List<String> links
) {
}
