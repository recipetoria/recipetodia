package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.ApiResponse;
import com.jit.rec.recipetoria.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/client/ingredients",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<ApiResponse> createIngredient(@RequestBody @Valid IngredientDTO newIngredientInfo) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .message("Ingredient created")
                        .data(Map.of("createdIngredientDTO", ingredientService.createIngredient(newIngredientInfo)))
                        .build()
        );
    }

    @GetMapping("/{ingredientId}")
    public ResponseEntity<ApiResponse> getIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Ingredient retrieved")
                        .data(Map.of("ingredientDTO", ingredientService.getIngredientById(ingredientId)))
                        .build()
        );
    }
    @PatchMapping("/{ingredientId}")
    public ResponseEntity<ApiResponse> updateIngredientById(@PathVariable("ingredientId") Long ingredientId,
                                                            @RequestBody @Valid IngredientDTO updatedIngredientInfo) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Ingredient updated")
                        .data(Map.of("updatedIngredientDTO",
                                ingredientService.updateIngredientById(ingredientId, updatedIngredientInfo)))
                        .build()
        );
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<ApiResponse> deleteIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        ingredientService.deleteIngredientById(ingredientId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Ingredient with ID " + ingredientId + " has been deleted!")
                        .build()
        );
    }
}
