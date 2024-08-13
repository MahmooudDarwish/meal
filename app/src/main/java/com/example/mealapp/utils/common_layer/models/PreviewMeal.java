package com.example.mealapp.utils.common_layer.models;

import com.google.gson.annotations.SerializedName;

public class PreviewMeal {

    final private String idMeal;
    final private String strMeal;
    final private String strMealThumb;


    public PreviewMeal(String idMeal, String strMeal ,String strMealThumb) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
    }
    public String getIdMeal() {
        return idMeal;
    }


    public String getStrMeal() {
        return strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

}
