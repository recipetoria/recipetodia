package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.NewIngredientRequest;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return applicationUserService.getAllIngredients();
    }

    @PostMapping
    public void createIngredient(@RequestBody NewIngredientRequest newIngredientRequest) {
        applicationUserService.createIngredient(newIngredientRequest);
    }
}
