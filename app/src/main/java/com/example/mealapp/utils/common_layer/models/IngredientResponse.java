package com.example.mealapp.utils.common_layer.models;


import java.util.List;

public class IngredientResponse {
    private List<Ingredient> meals;

    public List<Ingredient> getMeals() {
        return meals;
    }

    public void setMeals(List<Ingredient> meals) {
        this.meals = meals;
    }
}
