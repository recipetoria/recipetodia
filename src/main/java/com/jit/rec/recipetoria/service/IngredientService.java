package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.dto.NewIngredientRequest;
import com.jit.rec.recipetoria.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public void createIngredient(NewIngredientRequest newIngredientRequest) {
        Ingredient ingredient = new Ingredient();

        ingredient.setName(newIngredientRequest.getName());
        ingredient.setAmount(newIngredientRequest.getAmount());
        ingredient.setMeasurementUnit(newIngredientRequest.getMeasurementUnit());

        ingredientRepository.save(ingredient);
    }

    public Ingredient getIngredientById(Long ingredientId) {
        return ingredientRepository.findById(ingredientId).orElseThrow(() -> new IllegalStateException("NOT FOUND"));
    }

    public void updateIngredientById(Long ingredientId, Ingredient updatedIngredientInfo) {
        Ingredient ingredientToBeUpdated = getIngredientById(ingredientId);

        ingredientToBeUpdated.setName(updatedIngredientInfo.getName());
        ingredientToBeUpdated.setAmount(updatedIngredientInfo.getAmount());
        ingredientToBeUpdated.setMeasurementUnit(updatedIngredientInfo.getMeasurementUnit());

        ingredientRepository.save(ingredientToBeUpdated);
    }

    public void deleteIngredientById(Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }
}
