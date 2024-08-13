package com.example.mealapp.utils.network;

import com.example.mealapp.utils.common_layer.models.CountryResponse;
import com.example.mealapp.utils.common_layer.models.CategoryResponse;
import com.example.mealapp.utils.common_layer.models.DetailedMealResponse;
import com.example.mealapp.utils.common_layer.models.PreviewMealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService
{
    @GET("random.php")
    Call<PreviewMealResponse> getRandomMeal();

    @GET("categories.php")
    Call<CategoryResponse> getCategories();
    @GET("list.php?a=list")
    Call<CountryResponse> getCountries();

    @GET("lookup.php")
    Call<DetailedMealResponse> getMealDetails(@Query("i") String id);


}
