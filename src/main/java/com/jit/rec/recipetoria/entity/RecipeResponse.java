package com.jit.rec.recipetoria.entity;

import lombok.Data;

import java.util.List;

@Data
public class RecipeResponse {
    private String name;
    private String mainPhoto;
    private List<String> tags;
    private List<Ingredient> ingredients;
    private List<String> instructions;
    private List<String> instructionPhotos;
    private List<String> links;
}
