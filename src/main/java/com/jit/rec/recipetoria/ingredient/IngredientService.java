package com.jit.rec.recipetoria.ingredient;

import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.recipe.Recipe;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientDTOMapper ingredientDTOMapper;
    private final MessageSource messageSource;

    public Ingredient createIngredient(IngredientDTO newRecipeIngredientDTO) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Ingredient newRecipeIngredient = new Ingredient();

        newRecipeIngredient.setName(newRecipeIngredientDTO.name());
        newRecipeIngredient.setAmount(newRecipeIngredientDTO.amount());
        newRecipeIngredient.setMeasurementUnit(newRecipeIngredientDTO.measurementUnit());
        newRecipeIngredient.setApplicationUser(applicationUser);

        return ingredientRepository.save(newRecipeIngredient);
    }

    public Ingredient createIngredient(IngredientDTO newRecipeIngredientDTO, Recipe recipe) {
        Ingredient newRecipeIngredient = new Ingredient();

        newRecipeIngredient.setName(newRecipeIngredientDTO.name());
        newRecipeIngredient.setAmount(newRecipeIngredientDTO.amount());
        newRecipeIngredient.setMeasurementUnit(newRecipeIngredientDTO.measurementUnit());
        newRecipeIngredient.setRecipe(recipe);

        return ingredientRepository.save(newRecipeIngredient);
    }

    public IngredientDTO getIngredientDTOById(Long ingredientId) {
        return ingredientDTOMapper.apply(getIngredientById(ingredientId));
    }

    private Ingredient getIngredientById(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.ingredient.notFound", null, Locale.getDefault())));
    }

    public void updateIngredientById(Long ingredientId, IngredientDTO updatedIngredientInfo) {
        Ingredient ingredientToBeUpdated = getIngredientById(ingredientId);

        ingredientToBeUpdated.setName(updatedIngredientInfo.name());
        ingredientToBeUpdated.setAmount(updatedIngredientInfo.amount());
        ingredientToBeUpdated.setMeasurementUnit(updatedIngredientInfo.measurementUnit());

        ingredientRepository.save(ingredientToBeUpdated);
    }

    public void deleteIngredientById(Long ingredientId) {
        getIngredientById(ingredientId);
        ingredientRepository.deleteById(ingredientId);
    }

    public List<Ingredient> getAllIngredientsByApplicationUser() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ingredientRepository.findAllByApplicationUser(applicationUser);
    }
}
