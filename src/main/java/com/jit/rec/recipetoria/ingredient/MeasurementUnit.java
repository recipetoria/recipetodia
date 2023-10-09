package com.jit.rec.recipetoria.ingredient;

public enum MeasurementUnit {
    G("g"),
    KG("kg"),
    L("L"),
    ML("mL"),
    OZ("oz"),
    LB("lb"),
    PK("pk"),
    BX("bx"),
    BT("bt"),
    TBSP("tbsp"),
    TSP("tsp"),
    GL("gl"),
    PC("pc");

    public final String name;

    MeasurementUnit(String name) {
        this.name = name;
    }
}