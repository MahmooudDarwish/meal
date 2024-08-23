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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFoodPlans(List<MealPlan> mealPlans);


    @Delete
    void deleteFoodPlan(MealPlan foodPlan);

    @Query("SELECT * FROM food_plans WHERE idUser = :userId AND idMeal = :mealId")
    LiveData<MealPlan> getFoodPlan(String userId, String mealId);

    @Query("SELECT * FROM food_plans WHERE idUser = :userId")
    LiveData<List<MealPlan>> getAllMealPlansForUser(String userId);

    @Query("SELECT COUNT(*) > 0 FROM food_plans WHERE idMeal = :mealId AND idUser = :userId")
    boolean isMealPlanExists(String mealId, String userId);
    @Query("SELECT COUNT(*) FROM food_plans WHERE idMeal = :mealId AND idUser = :userId AND date = :date")
    int getMealPlanCount(String mealId, String userId, String date);

}

