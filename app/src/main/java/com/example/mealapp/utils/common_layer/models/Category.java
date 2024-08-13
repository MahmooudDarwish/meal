package com.example.mealapp.utils.common_layer.models;

public class Category {
    final private String idCategory;
    final private String strCategory;
    final private String strCategoryThumb;

    public Category(String idCategory, String strCategory, String strCategoryThumb) {
        this.idCategory = idCategory;
        this.strCategory = strCategory;
        this.strCategoryThumb = strCategoryThumb;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrCategoryThumb() {
        return strCategoryThumb;
    }

}