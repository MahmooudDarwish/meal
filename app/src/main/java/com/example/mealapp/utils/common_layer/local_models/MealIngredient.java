package com.example.mealapp.utils.common_layer.local_models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.example.mealapp.utils.common_layer.models.Ingredient;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(tableName = "ingredients", primaryKeys = {"userId", "ingredientName"})
public class MealIngredient {
    @NonNull
    private final String userId;
    @NonNull
    private final String ingredientName;
    private final String mealId;
    private final String ingredientImage;
    private final String measure;

    public MealIngredient(String mealId, @NonNull String ingredientName, String ingredientImage, String measure, @NonNull String userId) {
        this.mealId = mealId;
        this.ingredientName = ingredientName;
        this.ingredientImage = ingredientImage;
        this.measure = measure;
        this.userId = userId;
    }

    // Convenience constructor
    public MealIngredient(String mealId, Ingredient ingredient) {

        this(mealId, ingredient.getStrIngredient(),
                "https://www.themealdb.com/images/ingredients/" + ingredient.getStrIngredient() + "-Small" + ".png",
                ingredient.getQuantity(), UserSessionHolder.getInstance().getUser().getUid());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("mealId", mealId);
        map.put("ingredientName", ingredientName);
        map.put("ingredientImage", ingredientImage);
        map.put("measure", measure);
        return map;
    }

    public static MealIngredient fromMap(Map<String, Object> map) {
        String mealId = (String) map.get("mealId");
        String ingredientName = (String) map.get("ingredientName");
        String ingredientImage = (String) map.get("ingredientImage");
        String measure = (String) map.get("measure");
        String userId = (String) map.get("userId");

        return new MealIngredient(mealId, Objects.requireNonNull(ingredientName), ingredientImage, measure, userId);
    }
    @NonNull
    public String getUserId() {
        return userId;
    }

    // Getters and Setters
    public String getMealId() {
        return mealId;
    }

    @NonNull
    public String getIngredientName() {
        return ingredientName;
    }

    public String getIngredientImage() {
        return ingredientImage;
    }

    public String getMeasure() {
        return measure;
    }
}
