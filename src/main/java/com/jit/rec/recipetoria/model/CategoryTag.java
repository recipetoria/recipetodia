package com.jit.rec.recipetoria.model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class CategoryTag {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    Long id;
    String categoryName;
    @ManyToOne
    User categoryOwner;

}
