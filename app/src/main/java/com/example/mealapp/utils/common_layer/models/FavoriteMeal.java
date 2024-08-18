package com.example.mealapp.utils.common_layer.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "favorite_meals",
        primaryKeys = {"idUser", "idMeal"})
public class FavoriteMeal {
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

    public FavoriteMeal(DetailedMeal meal) {
        this.idMeal = meal.getIdMeal();
        this.strMeal = meal.getStrMeal();
        this.strCategory = meal.getStrCategory();;
        this.strArea = meal.getStrArea();
        this.strInstructions = meal.getStrInstructions();
        this.strMealThumb = meal.getStrMealThumb();
        this.strYoutube = meal.getStrYoutube();
        this.idUser = UserSessionHolder.getInstance().getUser().getUid();
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
