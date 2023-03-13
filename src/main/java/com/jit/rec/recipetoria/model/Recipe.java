package com.jit.rec.recipetoria.model;


import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class Recipe {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @ManyToOne
    private User owner;
    private String name;
    private String mainPhoto;




}
