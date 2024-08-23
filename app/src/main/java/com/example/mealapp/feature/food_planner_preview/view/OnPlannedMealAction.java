package com.example.mealapp.feature.food_planner_preview.view;

import com.example.mealapp.utils.common_layer.local_models.MealPlan;

public interface OnPlannedMealAction {
    void planMealClicked(String mealId);
    void deletePlanMealClicked(MealPlan id);

}
