package com.jit.rec.recipetoria.entity;

import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Recipe {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @ManyToOne
    private ApplicationUser applicationUser;
    private String name;
    private String mainPhoto;
//    @ManyToOne
//    private ShoppingList shoppingList;
}
