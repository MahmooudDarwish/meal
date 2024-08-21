package com.example.mealapp.feature.meal_details.view;

import com.example.mealapp.utils.common_layer.models.DetailedMeal;

public interface IMealDetails {
    void setUpMealDetails(DetailedMeal meal, boolean isFavorite, boolean isPlan);
    void onFailureResult(String errorMsg);

    void showToast(String msg);
    void updateFavoriteIcon (boolean isFavorite);

    void updateAddPlanBtnText (boolean isPlan);
    String getStringFromRes(int resId);


}
