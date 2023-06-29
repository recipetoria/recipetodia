package com.jit.rec.recipetoria.tag;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TagDTO(

        @Nullable
        Long id,

        @NotBlank(message = "{validation.tagDTO.name.NotBlank}")
        String name,

        @Nullable
        byte[] mainPhoto,

        @Nullable
        Long applicationUserId,

        @Nullable
        List<Long> recipeIds
) {
}
