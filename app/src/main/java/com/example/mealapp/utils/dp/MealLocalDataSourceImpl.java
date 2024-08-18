package com.example.mealapp.utils.dp;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealLocalDataSourceImpl implements MealLocalDataSource {

    private  static MealLocalDataSourceImpl mealLocalDataSource = null;
    private Context context;
    private final FavoriteMealDao favoriteMealDao;
    private final MealPlanDao mealPlanDao;
    private final FavoriteMealIngredientDao favoriteMealIngredientDao;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();


    private MealLocalDataSourceImpl(Context _context) {
        this.context = _context;
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
        favoriteMealDao = db.favoriteMealDao();
        mealPlanDao = db.mealPlanDao();
        favoriteMealIngredientDao = db.favoriteMealIngredientDao();
    }

    public static MealLocalDataSourceImpl getInstance(Context _context) {
        if (mealLocalDataSource == null) {
            mealLocalDataSource = new MealLocalDataSourceImpl(_context);
        }
        return mealLocalDataSource;
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
    public LiveData<List<FavoriteMeal>> getAllFavoriteMealsForUser(String userId) {
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
    public LiveData<List<MealPlan>> getAllMealPlansForUser(String userId) {
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
    public LiveData<List<FavoriteMealIngredient>> getIngredientsForMeal(String mealId) {
        return favoriteMealIngredientDao.getIngredientsForMeal(mealId);
    }
}

