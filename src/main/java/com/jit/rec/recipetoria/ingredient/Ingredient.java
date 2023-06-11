package com.jit.rec.recipetoria.ingredient;

import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.recipe.Recipe;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor

public class Ingredient {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    private String name;

    private Double amount;

    private MeasurementUnit measurementUnit;

    @ManyToOne
    private ApplicationUser applicationUser;

    @ManyToOne
    private Recipe recipe;
}
