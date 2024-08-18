package com.example.mealapp.utils.common_layer.local_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mealapp.utils.common_layer.models.Ingredient;

@Entity(tableName = "ingredients")
public class FavoriteMealIngredient {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String mealId;
    private final String ingredientName;
    private final String ingredientImage;
    private final String measure;

    // Constructor that Room will use
    public FavoriteMealIngredient( String mealId, String ingredientName, String ingredientImage, String measure) {
        this.mealId = mealId;
        this.ingredientName = ingredientName;
        this.ingredientImage = ingredientImage;
        this.measure = measure;
    }

    // Convenience constructor
    public FavoriteMealIngredient(String mealId, Ingredient ingredient) {
        this(mealId, ingredient.getStrIngredient(),
                "https://www.themealdb.com/images/ingredients/" + ingredient.getStrIngredient() + "-Small" + ".png",
                ingredient.getQuantity());
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealId() {
        return mealId;
    }

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
