package com.example.mealapp.utils.data_source_manager;

import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;
import com.example.mealapp.utils.network.MealRemoteDataSource;
import com.example.mealapp.utils.network.HomeNetworkDelegate;

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
    public void getAllCategories(HomeNetworkDelegate homeNetworkDelegate) {
        remoteSource.getAllCategoriesCall(homeNetworkDelegate);
    }

    @Override
    public void getAllCountries(HomeNetworkDelegate homeNetworkDelegate) {
        remoteSource.getAllCountriesCall(homeNetworkDelegate);

    }

    @Override
    public void getMealDetails(MealDetailsNetworkDelegate mealDetailsNetworkDelegate, String mealId) {
        remoteSource.getMealDetailsCall(mealDetailsNetworkDelegate, mealId);

    }

    @Override
    public void getMealsByCategory(HomeNetworkDelegate homeNetworkDelegate, String category) {
        remoteSource.getAllMealsByCategoryCall(homeNetworkDelegate, category);
    }

    @Override
    public void getMealsByCountry(HomeNetworkDelegate homeNetworkDelegate, String country) {
        remoteSource.getAllMealsByCountryCall(homeNetworkDelegate, country);
    }
}
