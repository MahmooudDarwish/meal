package com.example.mealapp.utils.network;

import android.util.Log;


import androidx.annotation.NonNull;

import com.example.mealapp.utils.common_layer.models.CategoryResponse;
import com.example.mealapp.utils.common_layer.models.CountryResponse;
import com.example.mealapp.utils.common_layer.models.DetailedMealResponse;
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
    public void getRandomMealCall(HomeNetworkDelegate homeNetworkDelegate) {
        Call<PreviewMealResponse> call = mealService.getRandomMeal();
        call.enqueue(new Callback<PreviewMealResponse>() {
            @Override
            public void onResponse(@NonNull Call<PreviewMealResponse> call, @NonNull Response<PreviewMealResponse> response) {
                if(response.isSuccessful() && response.body() != null && !response.body().getMeals().isEmpty()){
                    PreviewMeal meal = response.body().getMeals().get(0);
                    homeNetworkDelegate.onGetRandomMealSuccessResult(meal);
                }
                else {
                    homeNetworkDelegate.onFailureResult("Response unsuccessful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PreviewMealResponse> call, @NonNull Throwable throwable) {
                Log.e(TAG, "onFailure Getting Random Meal" + throwable.getMessage());
                homeNetworkDelegate.onFailureResult(throwable.getMessage());
            }
        });
    }

    @Override
    public void getAllCategoriesCall(HomeNetworkDelegate homeNetworkDelegate) {
        Call<CategoryResponse> call = mealService.getCategories();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    homeNetworkDelegate.onGetAllCategoriesSuccessResult(response.body().getCategories());
                } else {
                    homeNetworkDelegate.onFailureResult("Response unsuccessful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable throwable) {
                homeNetworkDelegate.onFailureResult(throwable.getMessage());
                Log.e(TAG, "onFailure: " + throwable.getMessage());
            }
        });
    }

    @Override
    public void getAllCountriesCall(HomeNetworkDelegate homeNetworkDelegate) {
        Log.i(TAG, "getAllCountriesCall: ");
        Call<CountryResponse> call = mealService.getCountries();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CountryResponse> call, @NonNull Response<CountryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "onResponse: " + response.body().getCountries());
                    homeNetworkDelegate.onGetAllCountriesSuccessResult(response.body().getCountries());
                } else {
                    homeNetworkDelegate.onFailureResult("Response unsuccessful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CountryResponse> call, @NonNull Throwable throwable) {
                homeNetworkDelegate.onFailureResult(throwable.getMessage());
                Log.e(TAG, "onFailure: " + throwable.getMessage());
            }
        });

    }

    @Override
    public void getMealDetailsCall(MealDetailsNetworkDelegate mealDetailsNetworkDelegate, String mealId) {
        Log.i(TAG, "getAllCountriesCall: ");
        Call<DetailedMealResponse> call = mealService.getMealDetails(mealId);
        call.enqueue(new Callback<DetailedMealResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailedMealResponse> call, @NonNull Response<DetailedMealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "onResponse: " + response.body().getMeals());
                    mealDetailsNetworkDelegate.onGetMealDetailsSuccessResult(response.body().getMeals().get(0));
                } else {
                    mealDetailsNetworkDelegate.onFailureResult("Response unsuccessful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailedMealResponse> call, @NonNull Throwable throwable) {
                mealDetailsNetworkDelegate.onFailureResult(throwable.getMessage());
                Log.e(TAG, "onFailure: " + throwable.getMessage());
            }
        });

    }

}
