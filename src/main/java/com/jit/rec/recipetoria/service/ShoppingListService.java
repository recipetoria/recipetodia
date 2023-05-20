package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.ApplicationUser;
import com.jit.rec.recipetoria.entity.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final IngredientService ingredientService;

    private ApplicationUser getApplicationUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public List<IngredientDTO> getAllIngredientsForShoppingList() {
        List<Ingredient> allIngredients = ingredientService.getAllIngredientsByApplicationUser();

        List<IngredientDTO> allIngredientDTOs = new ArrayList<>();
        for (Ingredient oneIngredient : allIngredients) {
            allIngredientDTOs.add(IngredientDTO.convertToDTO(oneIngredient));
        }

        return allIngredientDTOs;
    }

    public IngredientDTO createIngredientInShoppingList(IngredientDTO newIngredientInfo) {
        Ingredient createdIngredient = ingredientService.createIngredient(newIngredientInfo, getApplicationUser());
        return IngredientDTO.convertToDTO(createdIngredient);
    }

    public void deleteAllIngredientsFromShoppingList() {
        List<Ingredient> allIngredients = ingredientService.getAllIngredientsByApplicationUser();

        for (Ingredient oneIngredient : allIngredients) {
            ingredientService.deleteIngredientById(oneIngredient.getId());
        }
    }
}