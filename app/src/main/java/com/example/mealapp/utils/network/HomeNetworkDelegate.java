package com.example.mealapp.utils.network;

import com.example.mealapp.utils.common_layer.models.PreviewMeal;

import java.util.List;


public interface HomeNetworkDelegate {
     void onGetRandomMealSuccessResult(PreviewMeal previewMeal);

     void onGetRandomMealsSuccessResult(PreviewMeal previewMeals);


     void onFailureResult(String errorMsg);

}
