package com.jit.rec.recipetoria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jit.rec.recipetoria.dto.NewIngredientRequest;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;

import com.jit.rec.recipetoria.service.IngredientService;
import com.jit.rec.recipetoria.service.RecipeService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
public class Recipe {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    private String mainPhoto;
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "user_id")
    private ApplicationUser applicationUser;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "recipe_tags",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Ingredient> ingredientList;
    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> instructions;
    @ElementCollection
    private List<String> instructionPhotos;
    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> links;

    public Recipe(String name) {
        this.name = name;
    }

    public List<String> getRecipeTagNames(){
        List<String>recipeTagNames = new ArrayList<>();
        for (Tag t: tags){
           recipeTagNames.add(t.getName());
        }
        return recipeTagNames;
    }

}
