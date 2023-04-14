package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/client/ingredients",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<IngredientDTO> createIngredient(@RequestBody @Valid IngredientDTO newIngredientRequest) {
        IngredientDTO createdIngredientDTO = ingredientService.createIngredient(newIngredientRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdIngredientDTO);
    }

    @GetMapping("/{ingredientId}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        IngredientDTO ingredientDTO = ingredientService.getIngredientById(ingredientId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ingredientDTO);
    }

    @PatchMapping("/{ingredientId}")
    public ResponseEntity<IngredientDTO> updateIngredientById(@PathVariable("ingredientId") Long ingredientId,
                                                              @RequestBody @Valid IngredientDTO updatedIngredientInfo) {
        IngredientDTO updatedIngredientDTO = ingredientService.updateIngredientById(ingredientId, updatedIngredientInfo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedIngredientDTO);
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<String> deleteIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        ingredientService.deleteIngredientById(ingredientId);

        String message = "Ingredient with ID " + ingredientId + " has been deleted!";

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(message);
    }
}
