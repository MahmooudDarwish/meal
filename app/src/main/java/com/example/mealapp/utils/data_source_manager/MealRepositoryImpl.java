package com.example.mealapp.utils.data_source_manager;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.dp.IsFavoriteMealCallback;
import com.example.mealapp.utils.dp.MealLocalDataSource;
import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;
import com.example.mealapp.utils.network.MealRemoteDataSource;
import com.example.mealapp.utils.network.HomeNetworkDelegate;
import com.example.mealapp.utils.network.SearchNetworkDelegate;

import java.util.List;

public class MealRepositoryImpl implements MealRepository {

    private final MealRemoteDataSource remoteSource;
    private final MealLocalDataSource localSource;
    private static MealRepositoryImpl repo = null;

    public MealRepositoryImpl(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        this.remoteSource = remoteDataSource;
        this.localSource = localDataSource;
    }

    public static MealRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        if (repo == null) {
            repo = new MealRepositoryImpl(remoteDataSource, localDataSource);
        }
        return repo;
    }

    // Remote Data Source Methods
    @Override
    public void getRandomMeal(HomeNetworkDelegate homeNetworkDelegate) {
        remoteSource.getRandomMealCall(homeNetworkDelegate);
    }

    @Override
    public void getRandomMeals(HomeNetworkDelegate homeNetworkDelegate) {
        remoteSource.getRandomMealsCall(homeNetworkDelegate);
    }

    @Override
    public void getAllCategories(SearchNetworkDelegate searchNetworkDelegate) {
        remoteSource.getAllCategoriesCall(searchNetworkDelegate);
    }

    @Override
    public void getAllCountries(SearchNetworkDelegate searchNetworkDelegate) {
        remoteSource.getAllCountriesCall(searchNetworkDelegate);
    }

    @Override
    public void getAllIngredients(SearchNetworkDelegate searchNetworkDelegate) {
        remoteSource.getAllCIngredientsCall(searchNetworkDelegate);
    }

    @Override
    public void getMealDetails(MealDetailsNetworkDelegate mealDetailsNetworkDelegate, String mealId) {
        remoteSource.getMealDetailsCall(mealDetailsNetworkDelegate, mealId);
    }

    @Override
    public void getMealsByCategory(SearchNetworkDelegate searchNetworkDelegate, String category) {
        remoteSource.getAllMealsByCategoryCall(searchNetworkDelegate, category);
    }

    @Override
    public void getMealsByCountry(SearchNetworkDelegate searchNetworkDelegate, String country) {
        remoteSource.getAllMealsByCountryCall(searchNetworkDelegate, country);
    }

    @Override
    public void getMealsByIngredient(SearchNetworkDelegate searchNetworkDelegate, String ingredient) {
        remoteSource.getAllMealsByIngredientCall(searchNetworkDelegate, ingredient);
    }

    // Local Data Source Methods
    public void saveFavoriteMeal(FavoriteMeal favoriteMeal) {
        Log.i("MealRepositoryImpl", "saveFavoriteMeal: "+ favoriteMeal.getIdMeal());
        localSource.saveFavoriteMeal(favoriteMeal);
    }

    public void deleteFavoriteMeal(FavoriteMeal favoriteMeal) {
        localSource.deleteFavoriteMeal(favoriteMeal);
    }

    public LiveData<FavoriteMeal> getFavoriteMeal(String userId, String mealId) {
        return localSource.getFavoriteMeal(userId, mealId);
    }

    public LiveData<List<FavoriteMeal>> getAllFavoriteMealsForUser(String userId) {
        return localSource.getAllFavoriteMealsForUser(userId);
    }

    public void isMealFavorite(String userId, String mealId, IsFavoriteMealCallback callback) {
         localSource.isMealFavorite(userId, mealId, callback);
    }

    public void saveMealPlan(MealPlan mealPlan) {
        localSource.saveMealPlan(mealPlan);
    }

    public void deleteMealPlan(MealPlan mealPlan) {
        localSource.deleteMealPlan(mealPlan);
    }

    public LiveData<MealPlan> getMealPlan(String userId, String mealId) {
        return localSource.getMealPlan(userId, mealId);
    }

    public LiveData<List<MealPlan>> getAllMealPlansForUser(String userId) {
        return localSource.getAllMealPlansForUser(userId);
    }

    public void saveFavoriteMealIngredient(FavoriteMealIngredient ingredient) {
        localSource.saveFavoriteMealIngredient(ingredient);
    }

    public void deleteFavoriteMealIngredient(FavoriteMealIngredient ingredient) {
        localSource.deleteFavoriteMealIngredient(ingredient);
    }

    public LiveData<List<FavoriteMealIngredient>> getIngredientsForMeal(String mealId) {
        return localSource.getIngredientsForMeal(mealId);
    }
}
