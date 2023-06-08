package com.jit.rec.recipetoria.ingredient;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class IngredientDTOMapper implements Function<Ingredient, IngredientDTO> {

    @Override
    public IngredientDTO apply(Ingredient ingredient) {
        Long applicationUserId = null;
        if (ingredient.getApplicationUser() != null) {
            applicationUserId = ingredient.getApplicationUser().getId();
        }
        Long recipeId = null;
        if (ingredient.getRecipe() != null) {
            recipeId = ingredient.getRecipe().getId();
        }
        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getAmount(),
                ingredient.getMeasurementUnit(),
                applicationUserId,
                recipeId
        );
    }
}

