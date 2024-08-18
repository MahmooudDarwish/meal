package com.example.mealapp.utils.common_layer.local_models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;

@Entity(tableName = "food_plans", primaryKeys = {"idUser", "idMeal"})

public class MealPlan {

    @PrimaryKey
    @NonNull
    private final String idMeal;
    @NonNull
    private final String idUser;
    private final String strMeal;
    private final String strCategory;
    private final String strArea;
    private final String strInstructions;
    private final String strMealThumb;
    private final String strYoutube;
    private final String mealType;

    private final String date;




    public MealPlan(DetailedMeal meal, String date, String mealType) {
        this.date = date;
        this.mealType = mealType;
        this.idMeal = meal.getIdMeal();
        this.strMeal = meal.getStrMeal();
        this.strCategory = meal.getStrCategory();;
        this.strArea = meal.getStrArea();
        this.strInstructions = meal.getStrInstructions();
        this.strMealThumb = meal.getStrMealThumb();
        this.strYoutube = meal.getStrYoutube();
        this.idUser = UserSessionHolder.getInstance().getUser().getUid();
    }

    public String getMealType() {
        return mealType;
    }

    public String getDate() {
        return date;
    }

    @NonNull
    public String getIdMeal() { return idMeal; }

    @NonNull
    public String getIdUser() {
        return idUser;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrYoutube() {
        return strYoutube;
    }
}
