package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.ApiResponse;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/client", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllIngredients() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Ingredients retrieved")
                        .data(Map.of("allIngredientDTOs", applicationUserService.getAllIngredients()))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createIngredient(@Valid @RequestBody IngredientDTO newIngredientInfo) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .message("Ingredient created")
                        .data(Map.of("createdIngredientDTO",
                                applicationUserService.createIngredient(newIngredientInfo)))
                        .build()
        );
    }
}
