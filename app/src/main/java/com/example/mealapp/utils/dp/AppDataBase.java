package com.example.mealapp.utils.dp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;


@Database(entities = {FavoriteMeal.class, MealPlan.class, MealIngredient.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static volatile AppDataBase INSTANCE;

    public abstract FavoriteMealDao favoriteMealDao();

    public abstract MealPlanDao mealPlanDao();

    public abstract MealIngredientDao favoriteMealIngredientDao();
    public static synchronized AppDataBase getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
