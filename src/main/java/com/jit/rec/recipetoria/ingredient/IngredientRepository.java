package com.jit.rec.recipetoria.ingredient;

import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByApplicationUser(ApplicationUser applicationUser);
}
