package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.entity.Tag;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.*;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDTO {
    @Nullable
    private Long id;

    @NotNull(message = "name should not be null")
    private String name;

    @Nullable
    private Long applicationUserId;

    @Nullable
    private String mainPhoto;

    @Nullable
    private List<TagDTO> tagDTOs;

    @Nullable
    private List<IngredientDTO> ingredients;

    @Nullable
    private List<String> instructions;

    @Nullable
    private List<String> instructionPhotos;

    @Nullable
    private List<String> links;

    public List<IngredientDTO> getIngredients() {
        if (this.ingredients == null) {
            return this.ingredients = new ArrayList<>();
        } else
            return this.ingredients;
    }

    public static RecipeDTO convertToDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();

        List<TagDTO> tagDTOs = new ArrayList<>();
        if (recipe.getTags() != null) {
            List<Tag> tags = recipe.getTags();
            for (Tag tag : tags) {
                TagDTO newTagDTO = TagDTO.convertToTagDto(tag);
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
            recipeDTO.getIngredients().add(IngredientDTO.convertToDTO(ingredientFromRecipe));
        }
        return recipeDTO;
    }
}
