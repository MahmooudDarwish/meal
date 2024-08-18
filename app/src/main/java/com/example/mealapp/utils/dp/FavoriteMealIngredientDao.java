package com.example.mealapp.utils.dp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMealIngredient;

import java.util.List;

@Dao
public interface FavoriteMealIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMealIngredient(FavoriteMealIngredient ingredient);

    @Delete
    void deleteFavoriteMealIngredient(FavoriteMealIngredient ingredient);

    @Query("SELECT * FROM ingredients WHERE mealId = :mealId")
    LiveData<List<FavoriteMealIngredient>> getIngredientsForMeal(String mealId);

}

