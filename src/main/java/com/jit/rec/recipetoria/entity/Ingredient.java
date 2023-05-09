package com.jit.rec.recipetoria.entity;

import com.jit.rec.recipetoria.enumeration.MeasurementUnit;
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
