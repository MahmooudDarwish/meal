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

    @Delete
    void deleteFavoriteMeal(FavoriteMeal favoriteMeal);

    @Query("SELECT * FROM favorite_meals WHERE idUser = :userId AND idMeal = :mealId")
    FavoriteMeal getFavoriteMeal(String userId, String mealId);

    @Query("SELECT * FROM favorite_meals WHERE idUser = :userId")
    LiveData<List<FavoriteMeal>> getAllFavoriteMealsForUser(String userId);

    // 1 for ture and 0 for false
    @Query("SELECT COUNT(*) FROM favorite_meals WHERE idUser = :userId AND idMeal = :mealId")
    int isMealFavorite(String userId, String mealId);
}

