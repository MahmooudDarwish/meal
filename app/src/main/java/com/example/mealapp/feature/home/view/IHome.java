package com.example.mealapp.feature.home.view;

import com.example.mealapp.utils.common_layer.models.Category;
import com.example.mealapp.utils.common_layer.models.Country;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;

import java.util.List;

public interface IHome {

    void showRandomMeal(PreviewMeal meal);
    void showCategories(List<Category> categories);
    void showCountries(List<Country> countries);
    void onFailureResult(String errorMsg);
}
