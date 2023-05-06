package com.jit.rec.recipetoria.repository;

import com.jit.rec.recipetoria.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByTagsId(Long tagId);
}
