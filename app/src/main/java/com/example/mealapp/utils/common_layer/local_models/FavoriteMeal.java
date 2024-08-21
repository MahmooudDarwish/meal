package com.example.mealapp.utils.common_layer.local_models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.constants.ConstantKeys;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(tableName = "favorite_meals",
        primaryKeys = {"idUser", "idMeal"})
public class FavoriteMeal implements Serializable {
    @NonNull
    private final String idMeal;
    @NonNull
    private  String idUser;
    private final String strMeal;
    private final String strCategory;
    private final String strArea;
    private final String strInstructions;
    private final String strMealThumb;
    private final String strYoutube;

    // Constructor for Room (matching fields)
    public FavoriteMeal(@NonNull String idMeal, @NonNull String idUser, String strMeal,
                        String strCategory, String strArea, String strInstructions,
                        String strMealThumb, String strYoutube) {
        this.idMeal = idMeal;
        this.idUser = idUser;
        this.strMeal = strMeal;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strInstructions = strInstructions;
        this.strMealThumb = strMealThumb;
        this.strYoutube = strYoutube;
        this.idUser = UserSessionHolder.getInstance().getUser().getUid();
    }

    public FavoriteMeal(DetailedMeal meal) {
        this(meal.getIdMeal(), UserSessionHolder.getInstance().getUser().getUid() , meal.getStrMeal(),
                meal.getStrCategory(), meal.getStrArea(), meal.getStrInstructions(),
                meal.getStrMealThumb(), meal.getStrYoutube());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantKeys.KEY_ID_MEAL, idMeal);
        map.put(ConstantKeys.KEY_ID_USER, idUser);
        map.put(ConstantKeys.KEY_STR_MEAL, strMeal);
        map.put(ConstantKeys.KEY_STR_CATEGORY, strCategory);
        map.put(ConstantKeys.KEY_STR_AREA, strArea);
        map.put(ConstantKeys.KEY_STR_INSTRUCTIONS, strInstructions);
        map.put(ConstantKeys.KEY_STR_MEAL_THUMB, strMealThumb);
        map.put(ConstantKeys.KEY_STR_YOUTUBE, strYoutube);
        return map;
    }


    public static FavoriteMeal fromMap(Map<String, Object> map) {
        return new FavoriteMeal(
                (String) Objects.requireNonNull(map.get(ConstantKeys.KEY_ID_MEAL)),
                (String) Objects.requireNonNull(map.get(ConstantKeys.KEY_ID_USER)),
                (String) map.get(ConstantKeys.KEY_STR_MEAL),
                (String) map.get(ConstantKeys.KEY_STR_CATEGORY),
                (String) map.get(ConstantKeys.KEY_STR_AREA),
                (String) map.get(ConstantKeys.KEY_STR_INSTRUCTIONS),
                (String) map.get(ConstantKeys.KEY_STR_MEAL_THUMB),
                (String) map.get(ConstantKeys.KEY_STR_YOUTUBE)
        );
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
