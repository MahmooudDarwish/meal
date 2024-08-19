package com.example.mealapp.feature.food_planner_preview.view;

import com.example.mealapp.utils.common_layer.local_models.MealPlan;

import java.util.List;

public interface IFoodPlannerPreview {

    void showPlannedMeals(List<MealPlan> meals);

    void showLogin();
}


