package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.controllerapi.ApplicationUserApi;
import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.dto.Response;
import com.jit.rec.recipetoria.service.ApplicationUserService;
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
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ApplicationUserController implements ApplicationUserApi {

    private final ApplicationUserService applicationUserService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<Response> getAllIngredients() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.user.getAllIngredients", null, Locale.getDefault()))
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
                        .message(messageSource.getMessage(
                                "response.user.createIngredient", null, Locale.getDefault()))
                        .data(Map.of("createdIngredientDTO", applicationUserService.createIngredient(newIngredientInfo)))
                        .build());
    }
}
