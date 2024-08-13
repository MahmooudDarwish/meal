package com.example.mealapp.utils.data_source_manager;

import com.example.mealapp.utils.network.MealRemoteDataSource;
import com.example.mealapp.utils.network.NetworkDelegate;

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
    public void getRandomMeal(NetworkDelegate networkDelegate) {
        remoteSource.getRandomMealCall(networkDelegate);
    }

    @Override
    public void getAllCategories(NetworkDelegate networkDelegate) {
        remoteSource.getAllCategoriesCall(networkDelegate);
    }

    @Override
    public void getAllCountries(NetworkDelegate networkDelegate) {
        remoteSource.getAllCountriesCall(networkDelegate);

    }
}
