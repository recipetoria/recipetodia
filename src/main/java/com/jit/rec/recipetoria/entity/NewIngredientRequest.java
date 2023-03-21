package com.jit.rec.recipetoria.entity;

import lombok.Data;

@Data
public class NewIngredientRequest {

    private String name;
    private Double amount;
    private MeasurementUnit measurementUnit;
}
