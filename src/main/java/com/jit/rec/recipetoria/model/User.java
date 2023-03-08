package com.jit.rec.recipetoria.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    private String name;
    private String email;
    @OneToMany
    private List<Recipe> listOfRecipes;
    @OneToMany
    private List<CategoryTag> listOfCategories;


    @Id
    public Long getId() {
        return id;
    }
}
