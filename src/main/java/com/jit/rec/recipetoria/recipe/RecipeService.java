package com.jit.rec.recipetoria.recipe;

import com.jit.rec.recipetoria.ingredient.IngredientDTO;
import com.jit.rec.recipetoria.tag.TagDTO;
import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.ingredient.Ingredient;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.ingredient.IngredientService;
import com.jit.rec.recipetoria.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final TagService tagService;
    private final RecipeDTOMapper recipeDTOMapper;
    private final MessageSource messageSource;

    public List<RecipeDTO> getAllRecipes() {
        List<Recipe> allRecipes = recipeRepository.findAll();

        List<RecipeDTO> allRecipesDTOs = new ArrayList<>();
        for (Recipe oneRecipe : allRecipes) {
            allRecipesDTOs.add(recipeDTOMapper.apply(oneRecipe));
        }

        return allRecipesDTOs;
    }

    public RecipeDTO createRecipe(RecipeDTO newRecipeDTO) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Recipe newRecipe = new Recipe();

        newRecipe.setName(newRecipeDTO.name());
        newRecipe.setMainPhoto(newRecipeDTO.mainPhoto());
        newRecipe.setApplicationUser(applicationUser);
        newRecipe.setInstructions(newRecipeDTO.instructions());
        newRecipe.setInstructionPhotos(newRecipeDTO.instructionPhotos());
        newRecipe.setLinks(newRecipeDTO.links());

        Optional.ofNullable(newRecipeDTO.tagDTOs())
                .stream()
                .flatMap(Collection::stream)
                .map(TagDTO::id)
                .map(tagService::getTagById)
                .forEach(newRecipe.getTags()::add);

        newRecipe = recipeRepository.save(newRecipe);

        if (newRecipeDTO.ingredientDTOs() != null) {
            for (IngredientDTO ingredientDTO : newRecipeDTO.ingredientDTOs()) {
                Ingredient recipeIngredient = ingredientService.createIngredient(ingredientDTO, newRecipe);
                newRecipe.getIngredientList().add(recipeIngredient);
            }
        }

        return recipeDTOMapper.apply(newRecipe);
    }

    public RecipeDTO getRecipeDTOById(Long recipeId) {
        return recipeDTOMapper.apply(getRecipeById(recipeId));
    }

    private Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.recipe.notFound", null, Locale.getDefault())));
    }

    public void updateRecipeById(Long recipeId, RecipeDTO updatedRecipeInfo) {
        Recipe recipeToBeUpdated = getRecipeById(recipeId);

        recipeToBeUpdated.setName(updatedRecipeInfo.name());

        if (updatedRecipeInfo.tagDTOs() != null) {
            recipeToBeUpdated.setTags(new ArrayList<>());
        }

        Optional.ofNullable(updatedRecipeInfo.tagDTOs())
                .stream()
                .flatMap(Collection::stream)
                .map(TagDTO::id)
                .map(tagService::getTagById)
                .forEach(recipeToBeUpdated.getTags()::add);

        if (updatedRecipeInfo.ingredientDTOs() != null) {
            recipeToBeUpdated.setIngredientList(new ArrayList<>());
            for (IngredientDTO newIngredientDTO : updatedRecipeInfo.ingredientDTOs()) {
                Ingredient ingredient = new Ingredient();

                ingredient.setName(newIngredientDTO.name());
                ingredient.setAmount(newIngredientDTO.amount());
                ingredient.setMeasurementUnit(newIngredientDTO.measurementUnit());
                ingredient.setRecipe(recipeToBeUpdated);

                recipeToBeUpdated.getIngredientList().add(ingredient);
            }
        }

        recipeToBeUpdated.setMainPhoto(updatedRecipeInfo.mainPhoto());
        recipeToBeUpdated.setInstructions(updatedRecipeInfo.instructions());
        recipeToBeUpdated.setInstructionPhotos(updatedRecipeInfo.instructionPhotos());
        recipeToBeUpdated.setLinks(updatedRecipeInfo.links());
        recipeToBeUpdated.setId(recipeId);

        recipeRepository.save(recipeToBeUpdated);
    }

    public void deleteRecipeById(Long recipeId) {
        getRecipeById(recipeId);
        recipeRepository.deleteById(recipeId);
    }

    public List<RecipeDTO> getAllRecipesByTag(Long tagId) {
        List<Recipe> recipesByTag = recipeRepository.findByTagsId(tagId);

        List<RecipeDTO> recipeDTOs = new ArrayList<>();
        for (Recipe oneRecipe : recipesByTag) {
            RecipeDTO recipeDTO = new RecipeDTO(
                    oneRecipe.getId(),
                    oneRecipe.getName(),
                    oneRecipe.getMainPhoto(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            recipeDTOs.add(recipeDTO);
        }

        return recipeDTOs;
    }
}
