package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.NewIngredientRequest;
import com.jit.rec.recipetoria.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @PostMapping
    public void createIngredient(@RequestBody NewIngredientRequest newIngredientRequest) {
        ingredientService.createIngredient(newIngredientRequest);
    }

    @GetMapping("/{ingredientId}")
    public Ingredient getIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        return ingredientService.getIngredientById(ingredientId);
    }

    @PatchMapping("/{ingredientId}")
    public void updateIngredientById(@PathVariable("ingredientId") Long ingredientId,
                                     @RequestBody Ingredient updatedIngredientInfo) {
        ingredientService.updateIngredientById(ingredientId, updatedIngredientInfo);
    }

    @DeleteMapping("/{ingredientId}")
    public void deleteIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        ingredientService.deleteIngredientById(ingredientId);
    }
}
