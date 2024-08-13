package com.example.mealapp.utils.common_layer.models;

import java.util.List;

public class DetailedMealResponse {
     private final List<DetailedMeal> meals;

    public DetailedMealResponse(List<DetailedMeal> meals) {
        this.meals = meals;
    }

    public List<DetailedMeal> getMeals() {
            return meals;
        }
}
