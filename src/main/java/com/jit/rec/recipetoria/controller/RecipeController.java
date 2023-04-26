package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.RecipeDTO;
import com.jit.rec.recipetoria.entity.ApiResponse;
import com.jit.rec.recipetoria.service.RecipeService;
import com.jit.rec.recipetoria.swagger.RecipeControllerInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/client/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RecipeController implements RecipeControllerInterface {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRecipes() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Recipes retrieved")
                        .data(Map.of("allRecipesDTOs", recipeService.getAllRecipes()))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createRecipe(@RequestBody @Valid RecipeDTO newRecipeInfo) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .message("Recipe created")
                        .data(Map.of("createdRecipeDTO", recipeService.createRecipe(newRecipeInfo)))
                        .build()
        );
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<ApiResponse> getRecipeById(@PathVariable("recipeId") Long recipeId) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Recipe retrieved")
                        .data(Map.of("recipeDTO", recipeService.getRecipeById(recipeId)))
                        .build()
        );
    }

    @PatchMapping("/{recipeId}")
    public ResponseEntity<ApiResponse> updateRecipeById(@PathVariable("recipeId") Long recipeId,
                                                        @RequestBody @Valid RecipeDTO updatedRecipeInfo) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Recipe updated")
                        .data(Map.of("updatedRecipeDTO",
                                recipeService.updateRecipeById(recipeId, updatedRecipeInfo)))
                        .build()
        );
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<ApiResponse> deleteRecipeById(@PathVariable("recipeId") Long recipeId) {
        recipeService.deleteRecipeById(recipeId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Recipe with ID " + recipeId + " has been deleted!")
                        .build()
        );
    }
}
