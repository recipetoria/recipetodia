package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        List<IngredientDTO> ingredientDTOList = applicationUserService.getAllIngredients();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ingredientDTOList);
    }

    @PostMapping
    public ResponseEntity<IngredientDTO> createIngredient(@Valid @RequestBody IngredientDTO newIngredientRequest) {
        IngredientDTO createdIngredientDTO = applicationUserService.createIngredient(newIngredientRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdIngredientDTO);
    }
}
