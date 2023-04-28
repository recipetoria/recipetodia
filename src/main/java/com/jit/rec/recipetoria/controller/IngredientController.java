package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.controllerapi.IngredientApi;
import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.dto.Response;
import com.jit.rec.recipetoria.service.IngredientService;
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
@RequestMapping("/api/v1/client/ingredients")
@RequiredArgsConstructor
public class IngredientController implements IngredientApi {

    private final IngredientService ingredientService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<Response> createIngredient(@RequestBody @Valid IngredientDTO newIngredientInfo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .message(messageSource.getMessage(
                                "response.ingredient.createIngredient", null, Locale.getDefault()))
                        .data(Map.of("createdIngredientDTO", ingredientService.createIngredient(newIngredientInfo)))
                        .build());
    }

    @GetMapping("/{ingredientId}")
    public ResponseEntity<Response> getIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.ingredient.getIngredientById", null, Locale.getDefault()))
                        .data(Map.of("ingredientDTO", ingredientService.getIngredientById(ingredientId)))
                        .build());
    }

    @PatchMapping("/{ingredientId}")
    public ResponseEntity<Response> updateIngredientById(@PathVariable("ingredientId") Long ingredientId,
                                                         @RequestBody @Valid IngredientDTO updatedIngredientInfo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.ingredient.updateIngredientById", null, Locale.getDefault()))
                        .data(Map.of("updatedIngredientDTO",
                                ingredientService.updateIngredientById(ingredientId, updatedIngredientInfo)))
                        .build());
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Response> deleteIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        ingredientService.deleteIngredientById(ingredientId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message(messageSource.getMessage(
                                "response.ingredient.deleteIngredientById", null, Locale.getDefault()))
                        .build());
    }
}
