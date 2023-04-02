package com.jit.rec.recipetoria.entity;

import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Setter
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
    @ManyToMany (mappedBy = "recipes")
    private List<Tag> tags;
    @OneToMany
    private List<Ingredient> ingredientList;
    @ElementCollection
    private List<String> instructions;
    @ElementCollection
    private List<String> instructionPhotos;
    @ElementCollection
    private List<String> links;




}
