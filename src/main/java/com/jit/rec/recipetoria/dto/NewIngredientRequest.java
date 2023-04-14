package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.entity.MeasurementUnit;
import lombok.Data;

@Data
public class NewIngredientRequest {

    private String name;
    private Double amount;
    private MeasurementUnit measurementUnit;
}
