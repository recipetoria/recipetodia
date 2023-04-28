package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final MessageSource messageSource;

    public IngredientDTO createIngredient(IngredientDTO newIngredientInfo) {
        Ingredient ingredient = IngredientDTO.convertToIngredient(newIngredientInfo);
        ingredientRepository.save(ingredient);

        return IngredientDTO.convertToDTO(ingredient);
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
}
