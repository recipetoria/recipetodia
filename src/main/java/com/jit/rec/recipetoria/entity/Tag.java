package com.jit.rec.recipetoria.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "recipes")
@EqualsAndHashCode(exclude = "recipes")
public class Tag {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @NotNull
    private String name;

    @ManyToOne
    private ApplicationUser applicationUser;
    private String icon;

    @ManyToMany(mappedBy = "tags")
    private List<Recipe> recipes;
}
