package com.example.mealapp.utils.common_layer.local_models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.example.mealapp.utils.common_layer.models.Ingredient;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.constants.ConstantKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(tableName = "ingredients", primaryKeys = {"idUser", "ingredientName"})
public class MealIngredient {
    @NonNull
    private final String idUser;
    @NonNull
    private final String ingredientName;
    private final String idMeal;
    private final String ingredientImage;
    private final String measure;

    public MealIngredient(String idMeal, @NonNull String ingredientName, String ingredientImage, String measure, @NonNull String idUser) {
        this.idMeal = idMeal;
        this.ingredientName = ingredientName;
        this.ingredientImage = ingredientImage;
        this.measure = measure;
        this.idUser = idUser;
    }

    // Convenience constructor
    public MealIngredient(String mealId, Ingredient ingredient) {

        this(mealId, ingredient.getStrIngredient(), ConstantKeys.getIngredientImageUrl(ingredient.getStrIngredient()),
                ingredient.getQuantity(), UserSessionHolder.getInstance().getUser().getUid());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantKeys.KEY_ID_USER, idUser);
        map.put(ConstantKeys.KEY_ID_MEAL, idMeal);
        map.put(ConstantKeys.KEY_INGREDIENT_NAME, ingredientName);
        map.put(ConstantKeys.KEY_INGREDIENT_IMAGE, ingredientImage);
        map.put(ConstantKeys.KEY_MEASURE, measure);
        return map;
    }

    public static MealIngredient fromMap(Map<String, Object> map) {
        String mealId = (String) map.get(ConstantKeys.KEY_ID_MEAL);
        String ingredientName = (String) map.get(ConstantKeys.KEY_INGREDIENT_NAME);
        String ingredientImage = (String) map.get(ConstantKeys.KEY_INGREDIENT_IMAGE);
        String measure = (String) map.get(ConstantKeys.KEY_MEASURE);
        String idUser = (String) map.get(ConstantKeys.KEY_ID_USER);

        return new MealIngredient(mealId, Objects.requireNonNull(ingredientName), ingredientImage, measure, Objects.requireNonNull(idUser));
    }

    @NonNull
    public String getIdUser() {
        return idUser;
    }

    public String getIdMeal() {
        return idMeal;
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
