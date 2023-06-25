package com.jit.rec.recipetoria.applicationUser.shoppingList;

import com.jit.rec.recipetoria.ingredient.IngredientDTO;
import com.jit.rec.recipetoria.ingredient.Ingredient;
import com.jit.rec.recipetoria.ingredient.IngredientDTOMapper;
import com.jit.rec.recipetoria.ingredient.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final IngredientService ingredientService;
    private final IngredientDTOMapper ingredientDTOMapper;

    public List<IngredientDTO> getAllIngredientsForShoppingList() {
        List<Ingredient> allIngredients = ingredientService.getAllIngredientsByApplicationUser();

        List<IngredientDTO> allIngredientDTOs = new ArrayList<>();
        for (Ingredient oneIngredient : allIngredients) {
            allIngredientDTOs.add(ingredientDTOMapper.apply(oneIngredient));
        }

        return allIngredientDTOs;
    }

    public IngredientDTO createIngredientInShoppingList(IngredientDTO newIngredientInfo) {
        Ingredient createdIngredient = ingredientService.createIngredient(newIngredientInfo);
        return ingredientDTOMapper.apply(createdIngredient);
    }

    public void deleteAllIngredientFromShoppingList() {
        List<Ingredient> allIngredients = ingredientService.getAllIngredientsByApplicationUser();

        for (Ingredient oneIngredient : allIngredients) {
            ingredientService.deleteIngredientById(oneIngredient.getId());
        }
    }

}
