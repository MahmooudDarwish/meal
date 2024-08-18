package com.example.mealapp.utils.data_source_manager;

import androidx.lifecycle.LiveData;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.dp.IsFavoriteMealCallback;
import com.example.mealapp.utils.network.HomeNetworkDelegate;
import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;
import com.example.mealapp.utils.network.SearchNetworkDelegate;

import java.util.List;

public interface MealRepository {

     // Remote Data Source Methods
     void getRandomMeal(HomeNetworkDelegate homeNetworkDelegate);

     void getRandomMeals(HomeNetworkDelegate homeNetworkDelegate);

     void getAllCategories(SearchNetworkDelegate searchNetworkDelegate);

     void getAllCountries(SearchNetworkDelegate searchNetworkDelegate);

     void getAllIngredients(SearchNetworkDelegate searchNetworkDelegate);

     void getMealDetails(MealDetailsNetworkDelegate mealDetailsNetworkDelegate, String mealId);

     void getMealsByCategory(SearchNetworkDelegate searchNetworkDelegate, String category);

     void getMealsByCountry(SearchNetworkDelegate searchNetworkDelegate, String country);

     void getMealsByIngredient(SearchNetworkDelegate searchNetworkDelegate, String ingredient);

     // Local Data Source Methods
     void saveFavoriteMeal(FavoriteMeal favoriteMeal);

     void deleteFavoriteMeal(FavoriteMeal favoriteMeal);

     LiveData<FavoriteMeal> getFavoriteMeal(String userId, String mealId);

     LiveData<List<FavoriteMeal>> getAllFavoriteMealsForUser(String userId);

     void isMealFavorite(String userId, String mealId, IsFavoriteMealCallback callback);

     void saveMealPlan(MealPlan mealPlan);

     void deleteMealPlan(MealPlan mealPlan);

     LiveData<MealPlan> getMealPlan(String userId, String mealId);

     LiveData<List<MealPlan>> getAllMealPlansForUser(String userId);

     void saveFavoriteMealIngredient(FavoriteMealIngredient ingredient);

     void deleteFavoriteMealIngredient(FavoriteMealIngredient ingredient);

     LiveData<List<FavoriteMealIngredient>> getIngredientsForMeal(String mealId);
}
