package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.controllerapi.RecipeApi;
import com.jit.rec.recipetoria.dto.RecipeDTO;
import com.jit.rec.recipetoria.entity.Response;
import com.jit.rec.recipetoria.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/recipes")
@RequiredArgsConstructor
public class RecipeController implements RecipeApi {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<Response> getAllRecipes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message("Recipes retrieved")
                        .data(Map.of("allRecipesDTOs", recipeService.getAllRecipes()))
                        .build());
    }

    @PostMapping
    public ResponseEntity<Response> createRecipe(@RequestBody @Valid RecipeDTO newRecipeInfo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Recipe created")
                        .data(Map.of("createdRecipeDTO", recipeService.createRecipe(newRecipeInfo)))
                        .build());
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Response> getRecipeById(@PathVariable("recipeId") Long recipeId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message("Recipe retrieved")
                        .data(Map.of("recipeDTO", recipeService.getRecipeById(recipeId)))
                        .build());
    }

    @PatchMapping("/{recipeId}")
    public ResponseEntity<Response> updateRecipeById(@PathVariable("recipeId") Long recipeId,
                                                     @RequestBody @Valid RecipeDTO updatedRecipeInfo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message("Recipe updated")
                        .data(Map.of("updatedRecipeDTO",
                                recipeService.updateRecipeById(recipeId, updatedRecipeInfo)))
                        .build());
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Response> deleteRecipeById(@PathVariable("recipeId") Long recipeId) {
        recipeService.deleteRecipeById(recipeId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message("Recipe with ID " + recipeId + " has been deleted!")
                        .build());
    }
}
