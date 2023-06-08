package com.jit.rec.recipetoria.recipe;

import com.jit.rec.recipetoria.tag.TagDTO;
import com.jit.rec.recipetoria.ingredient.IngredientDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.*;

@Data
public class RecipeDTO {
    @Nullable
    private Long id;

    @NotNull(message = "{validation.recipeDTO.name.NotNull}")
    private String name;

    @Nullable
    private Long applicationUserId;

    @Nullable
    private String mainPhoto;

    @Nullable
    private List<TagDTO> tagDTOs;

    @Nullable
    @Valid
    private List<IngredientDTO> ingredientDTOs;

    @Nullable
    private List<String> instructions;

    @Nullable
    private List<String> instructionPhotos;

    @Nullable
    private List<String> links;

    public List<IngredientDTO> getIngredientDTOs() {
        if (this.ingredientDTOs == null) {
            return this.ingredientDTOs = new ArrayList<>();
        } else
            return this.ingredientDTOs;
    }
}
