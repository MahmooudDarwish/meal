package com.example.mealapp.feature.meals_viewer.presenter;

public interface IMealsViewerPresenter {
    void onSearchQuery(String query);
    void loadMeals();
}

