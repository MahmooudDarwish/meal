package com.example.mealapp.utils.common_layer.local_models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;

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
        map.put("idMeal", idMeal);
        map.put("idUser", idUser);
        map.put("strMeal", strMeal);
        map.put("strCategory", strCategory);
        map.put("strArea", strArea);
        map.put("strInstructions", strInstructions);
        map.put("strMealThumb", strMealThumb);
        map.put("strYoutube", strYoutube);
        map.put("mealType", mealType);
        map.put("date", date);
        return map;
    }

    public static MealPlan fromMap(Map<String, Object> map) {
        String idMeal = (String) map.get("idMeal");
        String idUser = (String) map.get("idUser");
        String strMeal = (String) map.get("strMeal");
        String strCategory = (String) map.get("strCategory");
        String strArea = (String) map.get("strArea");
        String strInstructions = (String) map.get("strInstructions");
        String strMealThumb = (String) map.get("strMealThumb");
        String strYoutube = (String) map.get("strYoutube");
        String mealType = (String) map.get("mealType");
        String date = (String) map.get("date");

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
