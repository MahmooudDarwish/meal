package com.example.mealapp.feature.search.presenter;

public interface ISearchPresenter {

    void getAllCategories();
    void getAllCountries();
    void getMealsByCategory(String category);

    void getMealsByCountry(String country);


}
