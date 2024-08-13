package com.example.mealapp.utils.network;

public interface MealRemoteDataSource {
     void getRandomMealCall(NetworkDelegate networkDelegate);

     void getAllCategoriesCall(NetworkDelegate networkDelegate);

     void getAllCountriesCall(NetworkDelegate networkDelegate);


}