package com.example.mealapp.feature.search.view;


import com.example.mealapp.utils.common_layer.models.Category;
import com.example.mealapp.utils.common_layer.models.Country;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;

import java.util.List;

public interface ISearch {

    void showCategories(List<Category> categories);
    void showCountries(List<Country> countries);

    void countryClicked(List<PreviewMeal> meals);

    void categoryClicked(List<PreviewMeal> meals);

     void onFailureResult(String errorMsg);


}
