package com.example.mealapp.utils.network;

import com.example.mealapp.utils.common_layer.models.PreviewMeal;


public interface HomeNetworkDelegate {
     void onGetRandomMealSuccessResult(PreviewMeal previewMeal);


     void onFailureResult(String errorMsg);

}
