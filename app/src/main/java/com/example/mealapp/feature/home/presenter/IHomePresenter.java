package com.example.mealapp.feature.home.presenter;

public interface IHomePresenter {

     void getRandomMeal();
     void getAllCategories();
     void getAllCountries();
     void getMealsByCategory(String category);

     void getCurrentUser();

     void signOut();

     void getMealsByCountry(String country);

}

