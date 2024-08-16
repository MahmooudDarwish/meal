package com.example.mealapp.feature.meals_viewer.view;

import com.example.mealapp.utils.common_layer.models.PreviewMeal;

import java.util.List;

public interface IMealsViewer {
    void displayMeals(List<PreviewMeal> meals);
}
