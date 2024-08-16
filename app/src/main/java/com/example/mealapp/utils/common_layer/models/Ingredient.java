package com.example.mealapp.utils.common_layer.models;

public class Ingredient {
    final private String strIngredient;
    final private String quantity;

    public Ingredient(String name, String quantity) {
        this.strIngredient = name;
        this.quantity = quantity;
    }

    public String getStrIngredient() {
        return strIngredient;
    }

    public String getQuantity() {
        return quantity;
    }
}
