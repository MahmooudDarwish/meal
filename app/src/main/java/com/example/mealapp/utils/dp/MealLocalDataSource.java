package com.example.mealapp.utils.dp;

import androidx.lifecycle.LiveData;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;

import java.util.List;

public interface MealLocalDataSource {
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

