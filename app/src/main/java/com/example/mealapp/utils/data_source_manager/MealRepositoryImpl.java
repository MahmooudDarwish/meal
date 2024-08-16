package com.example.mealapp.utils.data_source_manager;

import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;
import com.example.mealapp.utils.network.MealRemoteDataSource;
import com.example.mealapp.utils.network.HomeNetworkDelegate;
import com.example.mealapp.utils.network.SearchNetworkDelegate;

public class MealRepositoryImpl implements MealRepository{

    MealRemoteDataSource remoteSource;
    //MealLocalDataSource localSource;
    private static MealRepositoryImpl repo = null;

    public  MealRepositoryImpl(MealRemoteDataSource remoteDataSource ){
        //MealLocalDataSource localDataSource
        remoteSource=  remoteDataSource;
       // localSource = localDataSource;
    }

    public static MealRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource ){
        //MealLocalDataSource localDataSource
        if(repo == null){
            //localDataSource
            repo = new MealRepositoryImpl(remoteDataSource );
        }
        return repo;

    }


    @Override
    public void getRandomMeal(HomeNetworkDelegate homeNetworkDelegate) {
        remoteSource.getRandomMealCall(homeNetworkDelegate);
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
}
