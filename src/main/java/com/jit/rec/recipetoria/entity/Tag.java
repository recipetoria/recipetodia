package com.jit.rec.recipetoria.entity;

import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Tag {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    private String name;
    @ManyToOne
    private ApplicationUser applicationUser;
    private String icon;

}
