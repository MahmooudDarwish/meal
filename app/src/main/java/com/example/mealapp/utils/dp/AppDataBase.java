package com.example.mealapp.utils.dp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mealapp.utils.common_layer.models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.models.FavoriteMealIngredient;
import com.example.mealapp.utils.common_layer.models.MealPlan;


@Database(entities = {FavoriteMeal.class, MealPlan.class, FavoriteMealIngredient.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static volatile AppDataBase INSTANCE;

    public abstract FavoriteMealDao favoriteMealDao();
    public abstract MealPlanDao foodPlanDao();
    public abstract FavoriteMealIngredientDao favoriteMealIngredientDao();
    public static AppDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

