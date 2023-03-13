package com.jit.rec.recipetoria.model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class Tag {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    private String name;
    @ManyToOne
    private User categoryOwner;
    private String icon;

}
