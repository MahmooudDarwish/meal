package com.example.mealapp.feature.home.view;

import com.example.mealapp.utils.common_layer.models.PreviewMeal;

public interface IHome {

    void showRandomMeal(PreviewMeal meal);
    void onFailureResult(String errorMsg);
}
