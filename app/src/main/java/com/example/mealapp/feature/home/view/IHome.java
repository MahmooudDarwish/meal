package com.example.mealapp.feature.home.view;

import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.User;

import java.util.List;


public interface IHome {

    void showRandomMeal(PreviewMeal meal);
    void getCurrentUserSuccessfully(User user);

    void onFailureResult(String errorMsg);

    void showRandomMeals(PreviewMeal meals);
}
