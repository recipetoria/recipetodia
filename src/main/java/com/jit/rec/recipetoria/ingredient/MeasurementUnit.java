package com.jit.rec.recipetoria.ingredient;

public enum MeasurementUnit {
    GRAM("gram"),
    KILOGRAM("kilogram"),
    LITER("liter"),
    MILLILITER("milliliter"),
    OUNCE("ounce"),
    POUND("pound"),
    PACK("pack"),
    BOX("box"),
    BOTTLE("bottle"),
    TABLESPOON("tablespoon"),
    TEASPOON("teaspoon"),
    GLASS("glass"),
    PIECE("piece");

    public final String name;

    MeasurementUnit(String name) {
        this.name = name;
    }
}
