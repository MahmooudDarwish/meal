package com.example.mealapp.feature.meal_details.presenter;

import com.example.mealapp.utils.common_layer.models.DetailedMeal;

public interface IMealDetailsPresenter {
    void getMealDetails(String mealId);
    void getFavoriteMeal(String mealId);
     void getMealPlan(String mealId);

    void toggleFavoriteStatus(DetailedMeal meal);

    void toggleMealPlan(DetailedMeal meal, String date, String mealType);

    }
