package com.jit.rec.recipetoria.ingredient;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record IngredientDTO(

        @Nullable
        Long id,

        @NotNull(message = "{validation.ingredientDTO.name.NotNull}")
        @Size(min = 1, max = 30, message = "{validation.ingredientDTO.name.Size}")
        String name,

        @Positive(message = "{validation.ingredientDTO.amount.Positive}")
        Double amount,

        MeasurementUnit measurementUnit,

        @Nullable
        Long applicationUserId,

        @Nullable
        Long recipeId
) {
}
