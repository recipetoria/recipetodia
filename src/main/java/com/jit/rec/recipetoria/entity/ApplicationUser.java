package com.jit.rec.recipetoria.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ApplicationUser {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    private String name;
    private String photo;
    private String email;
    private String password;
    private boolean locked = false;
    private boolean enabled = true;

    @OneToOne
    private ShoppingList shoppingList;
    @OneToMany
    private List<Recipe> listOfRecipes;
}
