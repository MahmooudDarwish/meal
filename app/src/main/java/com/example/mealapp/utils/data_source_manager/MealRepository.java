package com.example.mealapp.utils.data_source_manager;


import com.example.mealapp.utils.network.NetworkDelegate;


public interface MealRepository {

     void getRandomMeal(NetworkDelegate networkDelegate);

}
