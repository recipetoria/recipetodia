package com.jit.rec.recipetoria.security.applicationUser;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.dto.NewIngredientRequest;
import com.jit.rec.recipetoria.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final IngredientRepository ingredientRepository;

    public List<Ingredient> getAllIngredients() {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ingredientRepository.findAllByApplicationUser(applicationUser);
    }

    public void createIngredient(NewIngredientRequest newIngredientRequest) {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Ingredient ingredient = new Ingredient();

        ingredient.setName(newIngredientRequest.getName());
        ingredient.setAmount(newIngredientRequest.getAmount());
        ingredient.setMeasurementUnit(newIngredientRequest.getMeasurementUnit());
        ingredient.setApplicationUser(applicationUser);

        ingredientRepository.save(ingredient);
    }
}
