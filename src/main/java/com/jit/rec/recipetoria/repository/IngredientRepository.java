package com.jit.rec.recipetoria.repository;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByApplicationUser(ApplicationUser applicationUser);
}
