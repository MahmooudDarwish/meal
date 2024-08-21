package com.example.mealapp.utils.dp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealapp.utils.common_layer.local_models.MealIngredient;

import java.util.List;

@Dao
public interface MealIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMealIngredient(MealIngredient ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMealIngredients(List<MealIngredient> ingredients);


    @Query("DELETE FROM ingredients WHERE idMeal = :mealId")
    void deleteMealIngredient(String mealId);

    @Query("SELECT * FROM ingredients WHERE idMeal = :mealId")
    LiveData<List<MealIngredient>> getIngredientsForMeal(String mealId);

    @Query("SELECT * FROM ingredients WHERE idUser =:userId")
    LiveData<List<MealIngredient>> getMealIngredientsByUserId(String userId);
}

