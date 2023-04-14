package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.MeasurementUnit;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record IngredientDTO(
        @Nullable
        Long id,

        @NotNull(message = "Name cannot be null")
        @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
        String name,

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive")
        Double amount,

        MeasurementUnit measurementUnit,

        @Nullable
        Long applicationUserId
) {
    public static IngredientDTO convertToDTO(Ingredient ingredient) {
        Long applicationUserId = null;
        if (ingredient.getApplicationUser() != null) {
            applicationUserId = ingredient.getApplicationUser().getId();
        }
        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getAmount(),
                ingredient.getMeasurementUnit(),
                applicationUserId
        );
    }
}
