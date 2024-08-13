package com.example.mealapp.utils.common_layer.models;

public class Ingredient {
    final private String name;
    final private String quantity;

    public Ingredient(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }
}
