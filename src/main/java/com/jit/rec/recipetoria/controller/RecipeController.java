package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.controllerapi.RecipeApi;
import com.jit.rec.recipetoria.dto.RecipeDTO;
import com.jit.rec.recipetoria.dto.Response;
import com.jit.rec.recipetoria.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/recipes")
@RequiredArgsConstructor
public class RecipeController implements RecipeApi {

    private final RecipeService recipeService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<Response> getAllRecipes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.recipe.getAllRecipes", null, Locale.getDefault()))
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
                        .message(messageSource.getMessage(
                                "response.recipe.createRecipe", null, Locale.getDefault()))
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
                        .message(messageSource.getMessage(
                                "response.recipe.getRecipeById", null, Locale.getDefault()))
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
                        .message(messageSource.getMessage(
                                "response.recipe.updateRecipeById", null, Locale.getDefault()))
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
                        .message(messageSource.getMessage(
                                "response.recipe.deleteRecipeById", null, Locale.getDefault()))
                        .build());
    }

    @GetMapping("/tagged-by/{tagId}")
    public ResponseEntity<Response> getAllTaggedRecipes(@PathVariable("tagId") Long tagId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message("Recipes tagged with tag id " + tagId)
                        .data(Map.of("tagged recipes", recipeService.getAllRecipesByTag(tagId)))
                        .build());
    }

}
