package com.jit.rec.recipetoria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"ingredientList","tags", "instructions", "links"})
@ToString(exclude = {"ingredientList","tags", "instructions", "links"})
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

    @ManyToOne
    private ApplicationUser applicationUser;

    @ManyToMany
    private List<Tag> tags;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
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
        return Objects.requireNonNullElseGet(this.ingredientList, () -> this.ingredientList = new ArrayList<>());
    }

    public List<Tag> getTags() {
        return Objects.requireNonNullElseGet(this.tags, () -> this.tags = new ArrayList<>());
    }

    public List<String> getInstructions() {
        return Objects.requireNonNullElseGet(this.instructions, () -> this.instructions = new ArrayList<>());
    }

    public List<String> getInstructionPhotos() {
        return Objects.requireNonNullElseGet(this.instructionPhotos, () -> this.instructionPhotos = new ArrayList<>());
    }

    public List<String> getLinks() {
        return Objects.requireNonNullElseGet(this.links, () -> this.links = new ArrayList<>());
    }
}
