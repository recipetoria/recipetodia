package com.jit.rec.recipetoria.recipe;

import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.filestorage.FileStorageService;
import com.jit.rec.recipetoria.ingredient.Ingredient;
import com.jit.rec.recipetoria.ingredient.IngredientDTO;
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
    private final FileStorageService fileStorageService;

    @Override
    public RecipeDTO apply(Recipe recipe) {

        byte[] mainPhoto = "".getBytes();
        if (recipe.getMainPhoto() != null) {
            mainPhoto = fileStorageService.getPhoto(recipe.getMainPhoto());
        }

        Long applicationUserId = Optional.ofNullable(recipe.getApplicationUser())
                .map(ApplicationUser::getId)
                .orElse(null);

        List<TagDTO> tagDTOs = new ArrayList<>();
        if (recipe.getTags() != null) {
            List<Tag> tags = recipe.getTags();
            for (Tag tag : tags) {
                TagDTO newTagDTO = tagDTOMapper.apply(tag);
                tagDTOs.add(newTagDTO);
            }
        }

        List<IngredientDTO> ingredientDTOs = new ArrayList<>();
        if (recipe.getIngredientList() != null) {
            for (Ingredient ingredientFromRecipe : recipe.getIngredientList()) {
                ingredientDTOs.add(ingredientDTOMapper.apply(ingredientFromRecipe));
            }
        }

        List<byte[]> instructionPhotos = new ArrayList<>();
        for (String oneInstructionPhoto : recipe.getInstructionPhotos()) {
            byte[] oneInstructionPhotoBytes = "".getBytes();
            if (oneInstructionPhoto != null) {
                oneInstructionPhotoBytes = fileStorageService.getPhoto(oneInstructionPhoto);
            }
            instructionPhotos.add(oneInstructionPhotoBytes);
        }

        return new RecipeDTO(
                recipe.getId(),
                recipe.getName(),
                mainPhoto,
                applicationUserId,
                tagDTOs,
                ingredientDTOs,
                recipe.getInstructions(),
                instructionPhotos,
                recipe.getLinks(),
                null
        );
    }
}
