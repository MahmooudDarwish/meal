package com.example.mealapp.utils.data_source_manager;


import com.example.mealapp.utils.network.HomeNetworkDelegate;
import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;
import com.example.mealapp.utils.network.SearchNetworkDelegate;


public interface MealRepository {

     void getRandomMeal(HomeNetworkDelegate homeNetworkDelegate);

     void getRandomMeals(HomeNetworkDelegate homeNetworkDelegate);
     void getAllCategories(SearchNetworkDelegate searchNetworkDelegate);

     void getAllCountries(SearchNetworkDelegate searchNetworkDelegate);

     void getAllIngredients(SearchNetworkDelegate searchNetworkDelegate);

     void getMealDetails(MealDetailsNetworkDelegate mealDetailsNetworkDelegate, String mealId);

     void getMealsByCategory(SearchNetworkDelegate searchNetworkDelegate, String category);
     void getMealsByCountry(SearchNetworkDelegate searchNetworkDelegate, String country);

    void getMealsByIngredient(SearchNetworkDelegate searchPresenter, String ingredient);
}
