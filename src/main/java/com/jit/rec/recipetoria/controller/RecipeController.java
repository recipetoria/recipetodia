package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.RecipeDTO;
import com.jit.rec.recipetoria.entity.ApiResponse;
import com.jit.rec.recipetoria.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public List<RecipeDTO> getAllRecipes(){
        return recipeService.getAllRecipes();
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createNewRecipe(@RequestBody @Valid RecipeDTO newRecipe){
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .message("Recipe created")
                        .data(Map.of("createdRecipeDTO", recipeService.createNewRecipe(newRecipe)))
                        .build()
        );
    }

    @PatchMapping("/{recipeId}")
    public ResponseEntity<ApiResponse> updateRecipeById(@PathVariable("recipeId") Long recipeId,
                                                        @RequestBody @Valid RecipeDTO updatedRecipe){
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Recipe updated")
                        .data(Map.of("updatedRecipe", recipeService.updateRecipeById(recipeId, updatedRecipe)))
                        .build()
        );
    }


    @DeleteMapping("/{recipeId}")
    public ResponseEntity<ApiResponse> deleteRecipe(@PathVariable("recipeId") Long id){
        recipeService.deleteRecipeById(id);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Ingredient with ID " + id + " was deleted")
                        .build()
        );
    }

    @GetMapping("/{recipeId}")

    public ResponseEntity<ApiResponse> getRecipeById(@PathVariable("recipeId") Long id){
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("here is your recipe, bro")
                        .data(Map.of("recipeDTO", recipeService.getRecipeById(id)))
                        .build()
        );
    }

}
