package com.example.mealapp.utils.network;

public interface MealRemoteDataSource {
     void getRandomMealCall(HomeNetworkDelegate homeNetworkDelegate);

     void getAllCategoriesCall(SearchNetworkDelegate searchNetworkDelegate);

     void getAllCountriesCall(SearchNetworkDelegate searchNetworkDelegate);
     void getMealDetailsCall(MealDetailsNetworkDelegate mealDetailsNetworkDelegate, String mealId);

    void getAllMealsByCountryCall(SearchNetworkDelegate searchNetworkDelegate, String countryName);

    void getAllMealsByCategoryCall(SearchNetworkDelegate searchNetworkDelegate, String categoryName);

    void getAllCIngredientsCall(SearchNetworkDelegate searchNetworkDelegate);


}