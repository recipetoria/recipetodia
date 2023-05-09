package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.ApplicationUser;
import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.repository.IngredientRepository;
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
    private final MessageSource messageSource;

    public Ingredient createIngredient(IngredientDTO newRecipeIngredientDTO, ApplicationUser applicationUser) {
        Ingredient newRecipeIngredient = IngredientDTO.convertToIngredient(newRecipeIngredientDTO);
        newRecipeIngredient.setApplicationUser(applicationUser);

        return ingredientRepository.save(newRecipeIngredient);
    }

    public Ingredient createIngredient(IngredientDTO newRecipeIngredientDTO, Recipe recipe) {
        Ingredient newRecipeIngredient = IngredientDTO.convertToIngredient(newRecipeIngredientDTO);
        newRecipeIngredient.setRecipe(recipe);

        return ingredientRepository.save(newRecipeIngredient);
    }

    public IngredientDTO getIngredientById(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.ingredient.notFound", null, Locale.getDefault())));

        return IngredientDTO.convertToDTO(ingredient);
    }

    public IngredientDTO updateIngredientById(Long ingredientId, IngredientDTO updatedIngredientInfo) {
        Ingredient ingredientToBeUpdated = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.ingredient.notFound", null, Locale.getDefault())));

        ingredientToBeUpdated.setName(updatedIngredientInfo.name());
        ingredientToBeUpdated.setAmount(updatedIngredientInfo.amount());
        ingredientToBeUpdated.setMeasurementUnit(updatedIngredientInfo.measurementUnit());

        ingredientRepository.save(ingredientToBeUpdated);

        return IngredientDTO.convertToDTO(ingredientToBeUpdated);
    }

    public void deleteIngredientById(Long ingredientId) {
        if (!ingredientRepository.existsById(ingredientId)) {
            throw new ResourceNotFoundException(messageSource.getMessage(
                    "exception.ingredient.notFound", null, Locale.getDefault()));
        }
        ingredientRepository.deleteById(ingredientId);
    }

    List<Ingredient> getAllIngredientsByApplicationUser() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ingredientRepository.findAllByApplicationUser(applicationUser);
    }
}
