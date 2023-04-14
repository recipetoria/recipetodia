package com.jit.rec.recipetoria.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {
    private String name;
    private String mainPhoto;
    private List<String> tags;
    private List<NewIngredientRequest> ingredients;
    private List<String> instructions;
    private List<String> instructionPhotos;
    private List<String> links;
}
