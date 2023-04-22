package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.entity.Tag;

import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record TagDTO(
        @Nullable
        Long id,
        @Nullable
        String name,
        @Nullable
        String icon,
        @Nullable
        Long applicationUserId,
        @Nullable
        List<Long> recipeIds
) {
    public static TagDTO convertToTagDto(Tag tag) {

        //create a list of recipe ids
        List<Long> recipeIdList = new ArrayList<>();
        Optional.ofNullable(tag.getRecipes())
                .orElse(Collections.emptyList())
                .stream()
                .map(Recipe::getId)
                .forEach(recipeIdList::add);

        return new TagDTO(tag.getId(), tag.getName(), tag.getIcon(), tag.getApplicationUser().getId(), recipeIdList);
    }
}
