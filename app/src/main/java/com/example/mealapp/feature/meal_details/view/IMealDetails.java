package com.example.mealapp.feature.meal_details.view;

import com.example.mealapp.utils.common_layer.models.DetailedMeal;

public interface IMealDetails {
    void setUpMealDetails(DetailedMeal meal);
    void onFailureResult(String errorMsg);

}
