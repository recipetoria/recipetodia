package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.enumeration.MeasurementUnit;
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

    public static Ingredient convertToIngredient(IngredientDTO ingredientDTO) {
        Ingredient ingredient = new Ingredient();

        ingredient.setName(ingredientDTO.name());
        ingredient.setAmount(ingredientDTO.amount());
        ingredient.setMeasurementUnit(ingredientDTO.measurementUnit());

        return ingredient;
    }
}
