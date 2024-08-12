package com.example.mealapp.utils.common_layer.models;

public class PreviewMeal {

    final private String idMeal;
    final private String strMealThumb;


    public PreviewMeal(String idMeal, String strMealThumb) {
        this.idMeal = idMeal;
        this.strMealThumb = strMealThumb;
    }
    public String getIdMeal() {
        return idMeal;
    }


    public String getStrMealThumb() {
        return strMealThumb;
    }

}
