package com.example.mealapp.utils.data_source_manager;


import com.example.mealapp.utils.network.HomeNetworkDelegate;
import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;


public interface MealRepository {

     void getRandomMeal(HomeNetworkDelegate homeNetworkDelegate);
     void getAllCategories(HomeNetworkDelegate homeNetworkDelegate);

     void getAllCountries(HomeNetworkDelegate homeNetworkDelegate);

      void getMealDetails(MealDetailsNetworkDelegate mealDetailsNetworkDelegate, String mealId);

}
