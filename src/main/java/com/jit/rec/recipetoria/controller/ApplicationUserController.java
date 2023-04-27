package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.controllerapi.ApplicationUserApi;
import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.Response;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ApplicationUserController implements ApplicationUserApi {

    private final ApplicationUserService applicationUserService;

    @GetMapping
    public ResponseEntity<Response> getAllIngredients() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message("List of ingredients in the Shopping List retrieved successfully")
                        .data(Map.of("allIngredientDTOs", applicationUserService.getAllIngredients()))
                        .build());
    }

    @PostMapping
    public ResponseEntity<Response> createIngredient(@Valid @RequestBody IngredientDTO newIngredientInfo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Ingredient created")
                        .data(Map.of("createdIngredientDTO",
                                applicationUserService.createIngredient(newIngredientInfo)))
                        .build());
    }
}
