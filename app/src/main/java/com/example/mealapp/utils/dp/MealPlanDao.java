package com.example.mealapp.utils.dp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealapp.utils.common_layer.local_models.MealPlan;

import java.util.List;

@Dao
public interface MealPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFoodPlan(MealPlan mealPlan);

    @Delete
    void deleteFoodPlan(MealPlan foodPlan);

    @Query("SELECT * FROM food_plans WHERE idUser = :userId AND idMeal = :mealId")
    MealPlan getFoodPlan(String userId, String mealId);

    @Query("SELECT * FROM food_plans WHERE idUser = :userId")
    LiveData<List<MealPlan>> getAllMealPlansForUser(String userId);
}

