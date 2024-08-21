package com.example.mealapp.utils.data_source_manager;


import androidx.lifecycle.LiveData;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.dp.IsFavoriteMealCallback;
import com.example.mealapp.utils.dp.IsPlanMealCallback;
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
    @Override
    public void saveFavoriteMeal(FavoriteMeal favoriteMeal) {
        localSource.saveFavoriteMeal(favoriteMeal);
    }

    @Override
    public void addFavoriteMeals(List<FavoriteMeal> favoriteMeals) {
        localSource.addFavoriteMeals(favoriteMeals);
    }

    @Override
    public void deleteFavoriteMeal(FavoriteMeal favoriteMeal) {
        localSource.deleteFavoriteMeal(favoriteMeal);
    }
    @Override
    public LiveData<FavoriteMeal> getFavoriteMeal(String userId, String mealId) {
        return localSource.getFavoriteMeal(userId, mealId);
    }

    @Override
    public LiveData<List<FavoriteMeal>> getAllFavoriteMealsForUser(String userId) {
        return localSource.getAllFavoriteMealsForUser(userId);
    }
    @Override
    public void isMealFavorite(String userId, String mealId, IsFavoriteMealCallback callback) {
         localSource.isMealFavorite(userId, mealId, callback);
    }

    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        localSource.saveMealPlan(mealPlan);
    }

    @Override
    public void addMealPlans(List<MealPlan> mealPlans) {
            localSource.addMealPlans(mealPlans);
    }

    @Override
    public void deleteMealPlan(MealPlan mealPlan) {
        localSource.deleteMealPlan(mealPlan);
    }

    @Override
    public void isMealPlan(String userId, String mealId, IsPlanMealCallback callback) {
        localSource.isMealPlan(userId, mealId, callback);
    }
    @Override
    public LiveData<MealPlan> getMealPlan(String userId, String mealId) {
        return localSource.getMealPlan(userId, mealId);
    }
    @Override
    public LiveData<List<MealPlan>> getAllMealPlansForUser(String userId) {
        return localSource.getAllMealPlansForUser(userId);
    }
    @Override
    public void saveFavoriteMealIngredient(MealIngredient ingredient) {
        localSource.saveMealIngredient(ingredient);
    }

    @Override
    public void addMealIngredients(List<MealIngredient> ingredients) {
        localSource.addMealIngredients(ingredients);
    }

    @Override
    public void deleteMealIngredient(String mealId) {
        localSource.deleteMealIngredient(mealId);
    }
    @Override
    public LiveData<List<MealIngredient>> getIngredientsForMeal(String mealId) {
        return localSource.getIngredientsForMeal(mealId);
    }

    @Override
    public LiveData<List<MealIngredient>> getIngredientsForUser(String userId) {
        return localSource.getIngredientsForUserId(userId);
    }
}
