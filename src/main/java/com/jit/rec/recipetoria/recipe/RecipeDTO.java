package com.jit.rec.recipetoria.recipe;

import com.jit.rec.recipetoria.tag.TagDTO;
import com.jit.rec.recipetoria.ingredient.IngredientDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.*;

public record RecipeDTO(

        @Nullable
        Long id,

        @Nullable
        String name,

        @Nullable
        byte[] mainPhoto,

        @Nullable
        Long applicationUserId,

        @Nullable
        List<TagDTO> tagDTOs,

        @Nullable
        @Validated
        @Valid
        List<IngredientDTO> ingredientDTOs,

        @Nullable
        String instructions,

        @Nullable
        List<byte[]> instructionPhotos,

        @Nullable
        List<String> links,

        @Nullable
        String instructionPhotoToDelete
) {
}
