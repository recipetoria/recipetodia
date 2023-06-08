package com.jit.rec.recipetoria.recipe;

import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.ingredient.Ingredient;
import com.jit.rec.recipetoria.ingredient.IngredientDTOMapper;
import com.jit.rec.recipetoria.tag.Tag;
import com.jit.rec.recipetoria.tag.TagDTO;
import com.jit.rec.recipetoria.tag.TagDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeDTOMapper implements Function<Recipe, RecipeDTO> {

    private final IngredientDTOMapper ingredientDTOMapper;
    private final TagDTOMapper tagDTOMapper;

    @Override
    public RecipeDTO apply(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();

        List<TagDTO> tagDTOs = new ArrayList<>();
        if (recipe.getTags() != null) {
            List<Tag> tags = recipe.getTags();
            for (Tag tag : tags) {
                TagDTO newTagDTO = tagDTOMapper.apply(tag);
                tagDTOs.add(newTagDTO);
            }
        }
        recipeDTO.setTagDTOs(tagDTOs);

        recipeDTO.setApplicationUserId(
                Optional.ofNullable(recipe.getApplicationUser())
                        .map(ApplicationUser::getId)
                        .orElse(null)
        );
        recipeDTO.setId(recipe.getId());
        recipeDTO.setName(recipe.getName());
        recipeDTO.setMainPhoto(recipe.getMainPhoto());
        recipeDTO.setInstructions(recipe.getInstructions());
        recipeDTO.setInstructionPhotos((recipe.getInstructionPhotos()));
        recipeDTO.setLinks(recipe.getLinks());
        for (Ingredient ingredientFromRecipe : recipe.getIngredientList()) {
            recipeDTO.getIngredientDTOs().add(ingredientDTOMapper.apply(ingredientFromRecipe));
        }
        return recipeDTO;
    }
}
