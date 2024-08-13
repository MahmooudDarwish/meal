package com.example.mealapp.utils.network;

public interface MealRemoteDataSource {
     void getRandomMealCall(HomeNetworkDelegate homeNetworkDelegate);

     void getAllCategoriesCall(HomeNetworkDelegate homeNetworkDelegate);

     void getAllCountriesCall(HomeNetworkDelegate homeNetworkDelegate);
     void getMealDetailsCall(MealDetailsNetworkDelegate mealDetailsNetworkDelegate, String mealId);

    void getAllMealsByCountryCall(HomeNetworkDelegate homeNetworkDelegate, String countryName);

    void getAllMealsByCategoryCall(HomeNetworkDelegate homeNetworkDelegate, String categoryName);

}