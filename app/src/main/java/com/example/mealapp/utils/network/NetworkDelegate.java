package com.example.mealapp.utils.network;

import com.example.mealapp.utils.common_layer.models.Category;
import com.example.mealapp.utils.common_layer.models.Country;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;

import java.util.List;

public interface NetworkDelegate {
     void onGetRandomMealSuccessResult(PreviewMeal previewMeal);

     void onGetAllCategoriesSuccessResult(List<Category> categories);

     void onGetAllCountriesSuccessResult(List<Country> countries);

     void onFailureResult(String errorMsg);

}
