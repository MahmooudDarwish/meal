package com.example.mealapp.feature.meals_viewer.model;

import com.example.mealapp.utils.common_layer.models.PreviewMeal;

import java.util.List;

public class MealsViewModel {
    private List<PreviewMeal> meals;

    private static MealsViewModel instance;

    public static MealsViewModel getInstance() {
        if (instance == null) {
            instance = new MealsViewModel();
        }
        return instance;
    }

    public MealsViewModel() {
    }

    public void setMeals(List<PreviewMeal> mealList) {
        meals = mealList;
    }

    public List<PreviewMeal> getMeals() {
        return meals;
    }
}
