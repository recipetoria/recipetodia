package com.jit.rec.recipetoria.recipe;

import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByApplicationUser(ApplicationUser applicationUser);
    List<Recipe> findByTagsId(Long tagId);
}
