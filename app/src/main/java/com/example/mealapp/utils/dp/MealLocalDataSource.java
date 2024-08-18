package com.example.mealapp.utils.dp;

import com.example.mealapp.utils.common_layer.models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.models.FavoriteMealIngredient;
import com.example.mealapp.utils.common_layer.models.MealPlan;

import java.util.List;

public interface MealLocalDataSource {
    void saveFavoriteMeal(FavoriteMeal favoriteMeal);
    void deleteFavoriteMeal(FavoriteMeal favoriteMeal);
    FavoriteMeal getFavoriteMeal(String userId, String mealId);
    List<FavoriteMeal> getAllFavoriteMealsForUser(String userId);
    boolean isMealFavorite(String userId, String mealId);


    void saveMealPlan(MealPlan mealPlan);
    void deleteMealPlan(MealPlan mealPlan);
    MealPlan getMealPlan(String userId, String mealId);
    List<MealPlan> getAllMealPlansForUser(String userId);

    void saveFavoriteMealIngredient(FavoriteMealIngredient ingredient);
    void deleteFavoriteMealIngredient(FavoriteMealIngredient ingredient);
    List<FavoriteMealIngredient> getIngredientsForMeal(String mealId);
}

