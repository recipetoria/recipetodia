package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.repository.ApplicationUserRepository;
import com.jit.rec.recipetoria.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository,
                                  IngredientRepository ingredientRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.ingredientRepository = ingredientRepository;
    }
}
