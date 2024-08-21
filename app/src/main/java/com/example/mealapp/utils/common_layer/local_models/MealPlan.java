package com.example.mealapp.utils.common_layer.local_models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.constants.ConstantKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(tableName = "food_plans", primaryKeys = {"idUser", "idMeal"})
public class MealPlan {

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

    // Room requires this constructor with all the fields
    public MealPlan(@NonNull String idMeal, @NonNull String idUser, String strMeal,
                    String strCategory, String strArea, String strInstructions,
                    String strMealThumb, String strYoutube, String mealType, String date) {
        this.idMeal = idMeal;
        this.idUser = idUser;
        this.strMeal = strMeal;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strInstructions = strInstructions;
        this.strMealThumb = strMealThumb;
        this.strYoutube = strYoutube;
        this.mealType = mealType;
        this.date = date;
    }

    // This constructor can be used for convenience in your code
    public MealPlan(DetailedMeal meal, String date, String mealType) {
        this(meal.getIdMeal(), UserSessionHolder.getInstance().getUser().getUid(),
                meal.getStrMeal(), meal.getStrCategory(), meal.getStrArea(),
                meal.getStrInstructions(), meal.getStrMealThumb(), meal.getStrYoutube(),
                mealType, date);
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
        map.put(ConstantKeys.KEY_MEAL_TYPE, mealType);
        map.put(ConstantKeys.KEY_DATE, date);
        return map;
    }

    public static MealPlan fromMap(Map<String, Object> map) {
        String idMeal = (String) map.get(ConstantKeys.KEY_ID_MEAL);
        String idUser = (String) map.get(ConstantKeys.KEY_ID_USER);
        String strMeal = (String) map.get(ConstantKeys.KEY_STR_MEAL);
        String strCategory = (String) map.get(ConstantKeys.KEY_STR_CATEGORY);
        String strArea = (String) map.get(ConstantKeys.KEY_STR_AREA);
        String strInstructions = (String) map.get(ConstantKeys.KEY_STR_INSTRUCTIONS);
        String strMealThumb = (String) map.get(ConstantKeys.KEY_STR_MEAL_THUMB);
        String strYoutube = (String) map.get(ConstantKeys.KEY_STR_YOUTUBE);
        String mealType = (String) map.get(ConstantKeys.KEY_MEAL_TYPE);
        String date = (String) map.get(ConstantKeys.KEY_DATE);

        return new MealPlan(Objects.requireNonNull(idMeal), Objects.requireNonNull(idUser), strMeal, strCategory, strArea, strInstructions,
                strMealThumb, strYoutube, mealType, date);
    }
    @NonNull
    public String getIdMeal() {
        return idMeal;
    }

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

    public String getMealType() {
        return mealType;
    }

    public String getDate() {
        return date;
    }
}
