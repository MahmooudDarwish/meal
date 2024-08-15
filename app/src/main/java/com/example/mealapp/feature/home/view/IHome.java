package com.example.mealapp.feature.home.view;

import com.example.mealapp.utils.common_layer.models.Category;
import com.example.mealapp.utils.common_layer.models.Country;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.User;

import java.util.List;

public interface IHome {

    void showRandomMeal(PreviewMeal meal);
    void showCategories(List<Category> categories);
    void showCountries(List<Country> countries);

    void countryClicked(List<PreviewMeal> meals);

    void categoryClicked(List<PreviewMeal> meals);

    void getCurrentUserSuccessfully(User user);

    void onFailureResult(String errorMsg);
}
