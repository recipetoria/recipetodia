package com.jit.rec.recipetoria.security.applicationUser;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.NewIngredientRequest;
import com.jit.rec.recipetoria.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final IngredientRepository ingredientRepository;

    public ApplicationUser getApplicationUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public List<Ingredient> getAllIngredients() {
        ApplicationUser applicationUser = getApplicationUser();
        return ingredientRepository.findAllByApplicationUser(applicationUser);
    }

    public void createIngredient(NewIngredientRequest newIngredientRequest) {
        ApplicationUser applicationUser = getApplicationUser();
        Ingredient ingredient = new Ingredient();

        ingredient.setName(newIngredientRequest.getName());
        ingredient.setAmount(newIngredientRequest.getAmount());
        ingredient.setMeasurementUnit(newIngredientRequest.getMeasurementUnit());
        ingredient.setApplicationUser(applicationUser);

        ingredientRepository.save(ingredient);
    }
}
