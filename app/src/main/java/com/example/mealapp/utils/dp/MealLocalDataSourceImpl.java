package com.example.mealapp.utils.dp;

import com.example.mealapp.utils.common_layer.models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.models.FavoriteMealIngredient;
import com.example.mealapp.utils.common_layer.models.MealPlan;

import java.util.List;

public class MealLocalDataSourceImpl implements MealLocalDataSource {

    private final FavoriteMealDao favoriteMealDao;
    private final MealPlanDao mealPlanDao;
    private final FavoriteMealIngredientDao favoriteMealIngredientDao;

    public MealLocalDataSourceImpl(FavoriteMealDao favoriteMealDao,
                                   MealPlanDao mealPlanDao,
                                   FavoriteMealIngredientDao favoriteMealIngredientDao) {
        this.favoriteMealDao = favoriteMealDao;
        this.mealPlanDao = mealPlanDao;
        this.favoriteMealIngredientDao = favoriteMealIngredientDao;
    }

    @Override
    public void saveFavoriteMeal(FavoriteMeal favoriteMeal) {
        favoriteMealDao.insertFavoriteMeal(favoriteMeal);
    }

    @Override
    public void deleteFavoriteMeal(FavoriteMeal favoriteMeal) {
        favoriteMealDao.deleteFavoriteMeal(favoriteMeal);
    }

    @Override
    public FavoriteMeal getFavoriteMeal(String userId, String mealId) {
        return favoriteMealDao.getFavoriteMeal(userId, mealId);
    }

    @Override
    public List<FavoriteMeal> getAllFavoriteMealsForUser(String userId) {
        return favoriteMealDao.getAllFavoriteMealsForUser(userId);
    }

    @Override
    public boolean isMealFavorite(String userId, String mealId) {
        return favoriteMealDao.isMealFavorite(userId, mealId) > 0;

    }

    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        mealPlanDao.insertFoodPlan(mealPlan);
    }

    @Override
    public void deleteMealPlan(MealPlan mealPlan) {
        mealPlanDao.deleteFoodPlan(mealPlan);
    }

    @Override
    public MealPlan getMealPlan(String userId, String mealId) {
        return mealPlanDao.getFoodPlan(userId, mealId);
    }


    @Override
    public List<MealPlan> getAllMealPlansForUser(String userId) {
        return mealPlanDao.getAllMealPlansForUser(userId);
    }

    @Override
    public void saveFavoriteMealIngredient(FavoriteMealIngredient ingredient) {
        favoriteMealIngredientDao.insertFavoriteMealIngredient(ingredient);
    }

    @Override
    public void deleteFavoriteMealIngredient(FavoriteMealIngredient ingredient) {
        favoriteMealIngredientDao.deleteFavoriteMealIngredient(ingredient);
    }

    @Override
    public List<FavoriteMealIngredient> getIngredientsForMeal(String mealId) {
        return favoriteMealIngredientDao.getIngredientsForMeal(mealId);
    }
}

