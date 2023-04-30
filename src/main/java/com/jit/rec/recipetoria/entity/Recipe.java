package com.jit.rec.recipetoria.entity;

import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private ApplicationUser applicationUser;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn,
            inverseJoinColumns = @JoinColumn
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    //@JoinColumn(name = "recipe_id")
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

    public List<Ingredient> getIngredientList() {
        if (this.ingredientList == null) {
            return this.ingredientList = new ArrayList<>();
        } else
            return this.ingredientList;
    }

    public List<Tag> getTags() {
        if (this.tags == null) {
            return this.tags = new ArrayList<>();
        } else
            return this.tags;
    }

    public List<String> getInstructions() {
        if (this.instructions == null) {
            return this.instructions = new ArrayList<>();
        } else
            return this.instructions;
    }

    public List<String> getInstructionPhotos() {
        if (this.instructionPhotos == null) {
            return this.instructionPhotos = new ArrayList<>();
        } else
            return this.instructionPhotos;
    }

    public List<String> getLinks() {
        if (this.links == null) {
            return this.links = new ArrayList<>();
        } else
            return this.links;
    }
}
