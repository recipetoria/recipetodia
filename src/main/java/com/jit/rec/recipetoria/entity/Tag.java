package com.jit.rec.recipetoria.entity;

import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Tag {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @NotNull
    private String name;

    @ManyToOne (cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private ApplicationUser applicationUser;
    private String icon;

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    private List<Recipe> recipes;

}
