package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.NewIngredientRequest;
import com.jit.rec.recipetoria.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createIngredient(@RequestBody NewIngredientRequest newIngredientRequest) {
        Ingredient createdIngredient = ingredientService.createIngredient(newIngredientRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("id", createdIngredient.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
