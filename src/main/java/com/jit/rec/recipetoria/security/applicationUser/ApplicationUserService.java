package com.jit.rec.recipetoria.security.applicationUser;

import com.jit.rec.recipetoria.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final IngredientRepository ingredientRepository;

}
