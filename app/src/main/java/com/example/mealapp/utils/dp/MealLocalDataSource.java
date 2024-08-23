package com.example.mealapp.utils.dp;

import androidx.lifecycle.LiveData;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;

import java.util.List;

public interface MealLocalDataSource {
    void saveFavoriteMeal(FavoriteMeal favoriteMeal);
    void addFavoriteMeals(List<FavoriteMeal> favoriteMeals);
    void deleteFavoriteMeal(FavoriteMeal favoriteMeal);
    LiveData<FavoriteMeal> getFavoriteMeal(String userId, String mealId);
    LiveData<List<FavoriteMeal>> getAllFavoriteMealsForUser(String userId);
    void isMealFavorite(String userId, String mealId, IsFavoriteMealCallback callback);


    void saveMealPlan(MealPlan mealPlan);
    void addMealPlans(List<MealPlan> mealPlans);
    void deleteMealPlan(MealPlan mealPlan);
    LiveData<MealPlan> getMealPlan(String userId, String mealId);
    LiveData<List<MealPlan>> getAllMealPlansForUser(String userId);
    void isMealPlan(String userId, String mealId, IsPlanMealCallback callback);

    void saveMealIngredient(MealIngredient ingredient);
    void addMealIngredients(List<MealIngredient> ingredients);
    public void deleteMealIngredient(String mealId, String userId);
    LiveData<List<MealIngredient>> getIngredientsForMeal(String mealId);

    LiveData<List<MealIngredient>> getIngredientsForUserId(String userId);
}

