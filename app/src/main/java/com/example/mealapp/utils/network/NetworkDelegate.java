package com.example.mealapp.utils.network;

import com.example.mealapp.utils.common_layer.models.PreviewMeal;

public interface NetworkDelegate {
     void onSuccessResult(PreviewMeal previewMeal);
     void onFailureResult(String errorMsg);

}
