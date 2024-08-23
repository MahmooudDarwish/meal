package com.example.mealapp.feature.home.view;

import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.User;



public interface IHome {

    void showRandomMeal(PreviewMeal meal);
    void getCurrentUserSuccessfully(User user);
    void getCurrentUserFailed();

    void onSignOut();

    void onFailureResult(String errorMsg);

    void showRandomMeals(PreviewMeal meals);
}
