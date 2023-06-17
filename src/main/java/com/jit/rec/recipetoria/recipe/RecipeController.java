package com.jit.rec.recipetoria.recipe;

import com.jit.rec.recipetoria.dto.Response;
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
                        .data(Map.of("recipeDTO", recipeService.getRecipeDTOById(recipeId)))
                        .build());
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Response> updateRecipeById(@PathVariable("recipeId") Long recipeId,
                                                     @RequestBody @Valid RecipeDTO updatedRecipeInfo) {
        recipeService.updateRecipeById(recipeId, updatedRecipeInfo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.recipe.updateRecipeById", null, Locale.getDefault()))
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

    @GetMapping("/tagged/{tagId}")
    public ResponseEntity<Response> getAllRecipesByTag(@PathVariable("tagId") Long tagId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.recipe.getAllRecipesByTag", null, Locale.getDefault()))
                        .data(Map.of("allRecipesByTag", recipeService.getAllRecipesByTag(tagId)))
                        .build());
    }
}
