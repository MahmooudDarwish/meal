package com.example.mealapp.utils.network;

import android.util.Log;


import androidx.annotation.NonNull;

import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.PreviewMealResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSourceImpl implements  MealRemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String TAG = "MealRemoteDataSourceImpl";
    private final MealService mealService;
    private static MealRemoteDataSourceImpl client = null;

    private  MealRemoteDataSourceImpl(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mealService = retrofit.create(MealService.class);

    }

    public static MealRemoteDataSourceImpl getInstance(){
        if(client == null){
            client = new MealRemoteDataSourceImpl();
        }
        return client;
    }

    @Override
    public void makeNetworkCall(NetworkDelegate networkDelegate) {
        Call<PreviewMealResponse> call = mealService.getRandomMeal();
        call.enqueue(new Callback<PreviewMealResponse>() {
            @Override
            public void onResponse(@NonNull Call<PreviewMealResponse> call, @NonNull Response<PreviewMealResponse> response) {
                if(response.isSuccessful() && response.body() != null && !response.body().getMeals().isEmpty()){
                    PreviewMeal meal = response.body().getMeals().get(0);
                    networkDelegate.onSuccessResult(meal);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PreviewMealResponse> call, @NonNull Throwable throwable) {
                Log.e(TAG, "onFailure Getting Random Meal" + throwable.getMessage());
                networkDelegate.onFailureResult(throwable.getMessage());
            }
        });
    }

}
