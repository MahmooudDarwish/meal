package com.example.mealapp.utils.dp;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MealLocalDataSourceImpl implements MealLocalDataSource {

    private static MealLocalDataSourceImpl mealLocalDataSource = null;
    private final FavoriteMealDao favoriteMealDao;
    private final MealPlanDao mealPlanDao;
    private final MealIngredientDao mealIngredientDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private MealLocalDataSourceImpl(Context _context) {
        AppDataBase db = AppDataBase.getInstance(_context.getApplicationContext());
        favoriteMealDao = db.favoriteMealDao();
        mealPlanDao = db.mealPlanDao();
        mealIngredientDao = db.favoriteMealIngredientDao();
    }

    public static MealLocalDataSourceImpl getInstance(Context _context) {
        if (mealLocalDataSource == null) {
            mealLocalDataSource = new MealLocalDataSourceImpl(_context);
        }
        return mealLocalDataSource;
    }

    @Override
    public void saveFavoriteMeal(FavoriteMeal favoriteMeal) {
        Log.i("MealRepository", "Attempting to save favorite meal: " + favoriteMeal.getIdMeal());
        executor.execute(() -> {
            favoriteMealDao.insertFavoriteMeal(favoriteMeal);
            Log.i("MealRepository", "Favorite meal saved: " + favoriteMeal.getIdMeal());

        });
    }

    @Override
    public void addFavoriteMeals(List<FavoriteMeal> favoriteMeals) {
        executor.execute(() -> favoriteMealDao.insertFavoriteMeals(favoriteMeals));
    }


    @Override
    public void deleteFavoriteMeal(FavoriteMeal favoriteMeal) {
        executor.execute(() -> favoriteMealDao.deleteFavoriteMeal(favoriteMeal));
    }

    @Override
    public LiveData<FavoriteMeal> getFavoriteMeal(String userId, String mealId) {
        return favoriteMealDao.getFavoriteMeal(userId, mealId);
    }

    @Override
    public LiveData<List<FavoriteMeal>> getAllFavoriteMealsForUser(String userId) {
        return favoriteMealDao.getAllFavoriteMealsForUser(userId);
    }

    @Override
    public void isMealFavorite(String userId, String mealId, IsFavoriteMealCallback callback) {
        executor.execute(() -> {
            boolean isFavorite = favoriteMealDao.isFavoriteMeal(userId, mealId);
            Log.i("MealRepository", "Meal is favorite: " + isFavorite);

            callback.onFavoriteStatusResult(isFavorite);
        });
    }

    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        executor.execute(() -> mealPlanDao.insertFoodPlan(mealPlan));
    }

    @Override
    public void addMealPlans(List<MealPlan> mealPlans) {
        executor.execute(() -> mealPlanDao.insertFoodPlans(mealPlans));

    }

    @Override
    public void deleteMealPlan(MealPlan mealPlan) {
        executor.execute(() -> mealPlanDao.deleteFoodPlan(mealPlan));
    }

    @Override
    public LiveData<MealPlan> getMealPlan(String userId, String mealId) {
        return mealPlanDao.getFoodPlan(userId, mealId);
    }

    @Override
    public LiveData<List<MealPlan>> getAllMealPlansForUser(String userId) {
        return mealPlanDao.getAllMealPlansForUser(userId);
    }

    @Override
    public void isMealPlan(String userId, String mealId, IsPlanMealCallback callback) {
        executor.execute(() -> {
            boolean isPlan = mealPlanDao.isMealPlanExists(userId, mealId);
            Log.i("MealRepository", "Meal is plan: " + isPlan);

            callback.onPlanStatusResult(isPlan);
        });
    }

    @Override
    public void saveMealIngredient(MealIngredient ingredient) {
        executor.execute(() -> mealIngredientDao.insertMealIngredient(ingredient));
    }

    @Override
    public void addMealIngredients(List<MealIngredient> ingredients) {
        executor.execute(() -> mealIngredientDao.insertMealIngredients(ingredients));

    }

    @Override
    public void deleteMealIngredient(String mealId) {
        executor.execute(() -> mealIngredientDao.deleteMealIngredient(mealId));
    }

    @Override
    public LiveData<List<MealIngredient>> getIngredientsForMeal(String mealId) {
        return mealIngredientDao.getIngredientsForMeal(mealId);
    }

    @Override
    public LiveData<List<MealIngredient>> getIngredientsForUserId(String userId) {
        return mealIngredientDao.getMealIngredientsByUserId(userId);
    }
}
