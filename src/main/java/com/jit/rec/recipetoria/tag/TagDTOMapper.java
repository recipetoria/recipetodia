package com.jit.rec.recipetoria.tag;

import com.jit.rec.recipetoria.filestorage.FileStorageService;
import com.jit.rec.recipetoria.recipe.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TagDTOMapper implements Function<Tag, TagDTO> {

    private final FileStorageService fileStorageService;

    @Override
    public TagDTO apply(Tag tag) {
        List<Long> recipeIdList = new ArrayList<>();
        Optional.ofNullable(tag.getRecipes())
                .orElse(Collections.emptyList())
                .stream()
                .map(Recipe::getId)
                .forEach(recipeIdList::add);

        return new TagDTO(
                tag.getId(),
                tag.getName(),
                fileStorageService.getPhoto(tag.getMainPhoto()),
                tag.getApplicationUser().getId(),
                recipeIdList
        );
    }
}

