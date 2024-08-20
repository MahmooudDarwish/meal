package com.example.mealapp.utils.dp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;

import java.util.List;

@Dao
public interface FavoriteMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMeal(FavoriteMeal favoriteMeal);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMeals(List<FavoriteMeal> favoriteMeals);
    @Delete
    void deleteFavoriteMeal(FavoriteMeal favoriteMeal);

    @Query("SELECT * FROM favorite_meals WHERE idUser = :userId AND idMeal = :mealId")
    LiveData<FavoriteMeal> getFavoriteMeal(String userId, String mealId);

    @Query("SELECT * FROM favorite_meals WHERE idUser = :userId")
    LiveData<List<FavoriteMeal>> getAllFavoriteMealsForUser(String userId);

    // 1 for ture and 0 for false
    @Query("SELECT COUNT(*) > 0 FROM favorite_meals WHERE idMeal = :mealId AND idUser = :userId")
    boolean isFavoriteMeal(String mealId, String userId);
}

