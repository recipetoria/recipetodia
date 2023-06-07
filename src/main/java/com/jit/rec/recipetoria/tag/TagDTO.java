package com.jit.rec.recipetoria.tag;

import com.jit.rec.recipetoria.recipe.Recipe;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record TagDTO(
        @Nullable
        Long id,

        @NotBlank(message = "{validation.tagDTO.name.NotBlank}")
        String name,

        @Nullable
        String icon,

        @Nullable
        Long applicationUserId,

        @Nullable
        List<Long> recipeIds
) {
    public static TagDTO convertToDTO(Tag tag) {
        List<Long> recipeIdList = new ArrayList<>();
        Optional.ofNullable(tag.getRecipes())
                .orElse(Collections.emptyList())
                .stream()
                .map(Recipe::getId)
                .forEach(recipeIdList::add);

        return new TagDTO(
                tag.getId(),
                tag.getName(),
                tag.getIcon(),
                tag.getApplicationUser().getId(),
                recipeIdList);
    }
}
