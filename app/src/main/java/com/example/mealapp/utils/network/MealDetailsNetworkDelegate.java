package com.example.mealapp.utils.network;

import com.example.mealapp.utils.common_layer.models.DetailedMeal;

public interface MealDetailsNetworkDelegate {

    void onGetMealDetailsSuccessResult(DetailedMeal meal);
    void onFailureResult(String errorMsg);


}
