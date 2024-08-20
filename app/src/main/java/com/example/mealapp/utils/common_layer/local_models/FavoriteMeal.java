package com.example.mealapp.utils.common_layer.local_models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;

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
        map.put("idMeal", idMeal);
        map.put("idUser", idUser);
        map.put("strMeal", strMeal);
        map.put("strCategory", strCategory);
        map.put("strArea", strArea);
        map.put("strInstructions", strInstructions);
        map.put("strMealThumb", strMealThumb);
        map.put("strYoutube", strYoutube);
        return map;
    }

    public static FavoriteMeal fromMap(Map<String, Object> map) {
        return new FavoriteMeal(
                (String) Objects.requireNonNull(map.get("idMeal")),
                (String) Objects.requireNonNull(map.get("idUser")),
                (String) map.get("strMeal"),
                (String) map.get("strCategory"),
                (String) map.get("strArea"),
                (String) map.get("strInstructions"),
                (String) map.get("strMealThumb"),
                (String) map.get("strYoutube")
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
