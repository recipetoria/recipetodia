package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.ApplicationUser;
import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final IngredientRepository ingredientRepository;

    public ApplicationUser getApplicationUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public List<IngredientDTO> getAllIngredients() {
        List<Ingredient> allIngredients = ingredientRepository.findAllByApplicationUser(getApplicationUser());

        List<IngredientDTO> allIngredientDTOs = new ArrayList<>();
        for (Ingredient oneIngredient : allIngredients) {
            allIngredientDTOs.add(IngredientDTO.convertToDTO(oneIngredient));
        }

        return allIngredientDTOs;
    }

    public IngredientDTO createIngredient(IngredientDTO newIngredientInfo) {
        Ingredient ingredient = new Ingredient();

        ingredient.setName(newIngredientInfo.name());
        ingredient.setAmount(newIngredientInfo.amount());
        ingredient.setMeasurementUnit(newIngredientInfo.measurementUnit());
        ingredient.setApplicationUser(getApplicationUser());

        Ingredient createdIngredient = ingredientRepository.save(ingredient);

        return IngredientDTO.convertToDTO(createdIngredient);
    }
}
