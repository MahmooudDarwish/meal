package com.example.mealapp.utils.common_layer.models;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DetailedMeal {

    @SerializedName("idMeal")
    final private String idMeal;

    @SerializedName("strMeal")
    final private String strMeal;

    @SerializedName("strDrinkAlternate")
    final private String strDrinkAlternate;

    @SerializedName("strCategory")
    final private String strCategory;

    @SerializedName("strArea")
    final private String strArea;

    @SerializedName("strInstructions")
    final private String strInstructions;

    @SerializedName("strMealThumb")
    final private String strMealThumb;

    @SerializedName("strTags")
    final private String strTags;

    @SerializedName("strYoutube")
    final private String strYoutube;

    @SerializedName("strIngredient1")
     private String strIngredient1;

    @SerializedName("strIngredient2")
     private String strIngredient2;

    @SerializedName("strIngredient3")
     private String strIngredient3;

    @SerializedName("strIngredient4")
     private String strIngredient4;

    @SerializedName("strIngredient5")
     private String strIngredient5;

    @SerializedName("strIngredient6")
     private String strIngredient6;

    @SerializedName("strIngredient7")
     private String strIngredient7;

    @SerializedName("strIngredient8")
     private String strIngredient8;

    @SerializedName("strIngredient9")
      private String strIngredient9;

    @SerializedName("strIngredient10")
     private String strIngredient10;

    @SerializedName("strIngredient11")
     private String strIngredient11;

    @SerializedName("strIngredient12")
     private String strIngredient12;

    @SerializedName("strIngredient13")
     private String strIngredient13;

    @SerializedName("strIngredient14")
     private String strIngredient14;

    @SerializedName("strIngredient15")
     private String strIngredient15;

    @SerializedName("strIngredient16")
     private String strIngredient16;

    @SerializedName("strIngredient17")
     private String strIngredient17;

    @SerializedName("strIngredient18")
     private String strIngredient18;

    @SerializedName("strIngredient19")
     private String strIngredient19;

    @SerializedName("strIngredient20")
     private String strIngredient20;

    @SerializedName("strMeasure1")
     private String strMeasure1;

    @SerializedName("strMeasure2")
     private String strMeasure2;

    @SerializedName("strMeasure3")
     private String strMeasure3;

    @SerializedName("strMeasure4")
     private String strMeasure4;

    @SerializedName("strMeasure5")
     private String strMeasure5;

    @SerializedName("strMeasure6")
     private String strMeasure6;

    @SerializedName("strMeasure7")
     private String strMeasure7;

    @SerializedName("strMeasure8")
     private String strMeasure8;

    @SerializedName("strMeasure9")
     private String strMeasure9;

    @SerializedName("strMeasure10")
     private String strMeasure10;

    @SerializedName("strMeasure11")
     private String strMeasure11;

    @SerializedName("strMeasure12")
     private String strMeasure12;

    @SerializedName("strMeasure13")
     private String strMeasure13;

    @SerializedName("strMeasure14")
     private String strMeasure14;

    @SerializedName("strMeasure15")
     private String strMeasure15;

    @SerializedName("strMeasure16")
     private String strMeasure16;

    @SerializedName("strMeasure17")
     private String strMeasure17;

    @SerializedName("strMeasure18")
     private String strMeasure18;

    @SerializedName("strMeasure19")
     private String strMeasure19;

    @SerializedName("strMeasure20")
     private String strMeasure20;

    @SerializedName("strSource")
    final private String strSource;

    @SerializedName("strImageSource")
    final private String strImageSource;

    @SerializedName("strCreativeCommonsConfirmed")
    final private String strCreativeCommonsConfirmed;

    @SerializedName("dateModified")
    final private String dateModified;


    private String mealType;
    private String mealDate;

    public DetailedMeal(FavoriteMeal favoriteMeal, List<MealIngredient> ingredients) {
        this.idMeal = favoriteMeal.getIdMeal();
        this.strMeal = favoriteMeal.getStrMeal();
        this.strDrinkAlternate = null;
        this.strCategory = favoriteMeal.getStrCategory();
        this.strArea = favoriteMeal.getStrArea();
        this.strInstructions = favoriteMeal.getStrInstructions();
        this.strMealThumb = favoriteMeal.getStrMealThumb();
        this.strTags = null;
        this.strYoutube = favoriteMeal.getStrYoutube();
        this.strSource = null;
        this.strImageSource = null;
        this.strCreativeCommonsConfirmed = null;
        this.dateModified = null;

        // Populate ingredients
        for (int i = 0; i < ingredients.size(); i++) {
            MealIngredient ingredient = ingredients.get(i);
            try {
                String ingredientField = "strIngredient" + (i + 1);
                String measureField = "strMeasure" + (i + 1);

                this.getClass().getDeclaredField(ingredientField).set(this, ingredient.getIngredientName());
                this.getClass().getDeclaredField(measureField).set(this, ingredient.getMeasure());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public DetailedMeal(MealPlan mealPlan,  List<MealIngredient> ingredients){
        this.idMeal = mealPlan.getIdMeal();
        this.strMeal = mealPlan.getStrMeal();
        this.strDrinkAlternate = null;
        this.strCategory = mealPlan.getStrCategory();
        this.strArea = mealPlan.getStrArea();
        this.strInstructions = mealPlan.getStrInstructions();
        this.strMealThumb = mealPlan.getStrMealThumb();
        this.strTags = null;
        this.strYoutube = mealPlan.getStrYoutube();
        this.strSource = null;
        this.strImageSource = null;
        this.strCreativeCommonsConfirmed = null;
        this.dateModified = null;
        this.mealType = mealPlan.getMealType();
        this.mealDate = mealPlan.getDate();

        for (int i = 0; i < ingredients.size(); i++) {
            MealIngredient ingredient = ingredients.get(i);
            try {
                String ingredientField = "strIngredient" + (i + 1);
                String measureField = "strMeasure" + (i + 1);

                this.getClass().getDeclaredField(ingredientField).set(this, ingredient.getIngredientName());
                this.getClass().getDeclaredField(measureField).set(this, ingredient.getMeasure());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }


    public DetailedMeal(String idMeal, String strMeal, String strDrinkAlternate, String strCategory, String strArea, String strInstructions, String strMealThumb, String strTags, String strYoutube, String strIngredient1, String strIngredient2, String strIngredient3, String strIngredient4, String strIngredient5, String strIngredient6, String strIngredient7, String strIngredient8, String strIngredient9, String strIngredient10, String strIngredient11, String strIngredient12, String strIngredient13, String strIngredient14, String strIngredient15, String strIngredient16, String strIngredient17, String strIngredient18, String strIngredient19, String strIngredient20, String strMeasure1, String strMeasure2, String strMeasure3, String strMeasure4, String strMeasure5, String strMeasure6, String strMeasure7, String strMeasure8, String strMeasure9, String strMeasure10, String strMeasure11, String strMeasure12, String strMeasure13, String strMeasure14, String strMeasure15, String strMeasure16, String strMeasure17, String strMeasure18, String strMeasure19, String strMeasure20, String strSource, String strImageSource, String strCreativeCommonsConfirmed, String dateModified) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strDrinkAlternate = strDrinkAlternate;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strInstructions = strInstructions;
        this.strMealThumb = strMealThumb;
        this.strTags = strTags;
        this.strYoutube = strYoutube;
        this.strIngredient1 = strIngredient1;
        this.strIngredient2 = strIngredient2;
        this.strIngredient3 = strIngredient3;
        this.strIngredient4 = strIngredient4;
        this.strIngredient5 = strIngredient5;
        this.strIngredient6 = strIngredient6;
        this.strIngredient7 = strIngredient7;
        this.strIngredient8 = strIngredient8;
        this.strIngredient9 = strIngredient9;
        this.strIngredient10 = strIngredient10;
        this.strIngredient11 = strIngredient11;
        this.strIngredient12 = strIngredient12;
        this.strIngredient13 = strIngredient13;
        this.strIngredient14 = strIngredient14;
        this.strIngredient15 = strIngredient15;
        this.strIngredient16 = strIngredient16;
        this.strIngredient17 = strIngredient17;
        this.strIngredient18 = strIngredient18;
        this.strIngredient19 = strIngredient19;
        this.strIngredient20 = strIngredient20;
        this.strMeasure1 = strMeasure1;
        this.strMeasure2 = strMeasure2;
        this.strMeasure3 = strMeasure3;
        this.strMeasure4 = strMeasure4;
        this.strMeasure5 = strMeasure5;
        this.strMeasure6 = strMeasure6;
        this.strMeasure7 = strMeasure7;
        this.strMeasure8 = strMeasure8;
        this.strMeasure9 = strMeasure9;
        this.strMeasure10 = strMeasure10;
        this.strMeasure11 = strMeasure11;
        this.strMeasure12 = strMeasure12;
        this.strMeasure13 = strMeasure13;
        this.strMeasure14 = strMeasure14;
        this.strMeasure15 = strMeasure15;
        this.strMeasure16 = strMeasure16;
        this.strMeasure17 = strMeasure17;
        this.strMeasure18 = strMeasure18;
        this.strMeasure19 = strMeasure19;
        this.strMeasure20 = strMeasure20;
        this.strSource = strSource;
        this.strImageSource = strImageSource;
        this.strCreativeCommonsConfirmed = strCreativeCommonsConfirmed;
        this.dateModified = dateModified;
    }

    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();


        if (strIngredient1 != null && !strIngredient1.isEmpty())
            ingredients.add(new Ingredient(strIngredient1, strMeasure1));
        if (strIngredient2 != null && !strIngredient2.isEmpty())
            ingredients.add(new Ingredient(strIngredient2, strMeasure2));
        if (strIngredient3 != null && !strIngredient3.isEmpty())
            ingredients.add(new Ingredient(strIngredient3, strMeasure3));
        if (strIngredient4 != null && !strIngredient4.isEmpty())
            ingredients.add(new Ingredient(strIngredient4, strMeasure4));
        if (strIngredient5 != null && !strIngredient5.isEmpty())
            ingredients.add(new Ingredient(strIngredient5, strMeasure5));
        if (strIngredient6 != null && !strIngredient6.isEmpty())
            ingredients.add(new Ingredient(strIngredient6, strMeasure6));
        if (strIngredient7 != null && !strIngredient7.isEmpty())
            ingredients.add(new Ingredient(strIngredient7, strMeasure7));
        if (strIngredient8 != null && !strIngredient8.isEmpty())
            ingredients.add(new Ingredient(strIngredient8, strMeasure8));
        if (strIngredient9 != null && !strIngredient9.isEmpty())
            ingredients.add(new Ingredient(strIngredient9, strMeasure9));
        if (strIngredient10 != null && !strIngredient10.isEmpty())
            ingredients.add(new Ingredient(strIngredient10, strMeasure10));
        if (strIngredient11 != null && !strIngredient11.isEmpty())
            ingredients.add(new Ingredient(strIngredient11, strMeasure11));
        if (strIngredient12 != null && !strIngredient12.isEmpty())
            ingredients.add(new Ingredient(strIngredient12, strMeasure12));
        if (strIngredient13 != null && !strIngredient13.isEmpty())
            ingredients.add(new Ingredient(strIngredient13, strMeasure13));
        if (strIngredient14 != null && !strIngredient14.isEmpty())
            ingredients.add(new Ingredient(strIngredient14, strMeasure14));
        if (strIngredient15 != null && !strIngredient15.isEmpty())
            ingredients.add(new Ingredient(strIngredient15, strMeasure15));
        if (strIngredient16 != null && !strIngredient16.isEmpty())
            ingredients.add(new Ingredient(strIngredient16, strMeasure16));
        if (strIngredient17 != null && !strIngredient17.isEmpty())
            ingredients.add(new Ingredient(strIngredient17, strMeasure17));
        if (strIngredient18 != null && !strIngredient18.isEmpty())
            ingredients.add(new Ingredient(strIngredient18, strMeasure18));
        if (strIngredient19 != null && !strIngredient19.isEmpty())
            ingredients.add(new Ingredient(strIngredient19, strMeasure19));
        if (strIngredient20 != null && !strIngredient20.isEmpty())
            ingredients.add(new Ingredient(strIngredient20, strMeasure20));

        return ingredients;
    }



    public String getIdMeal() {
        return idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrDrinkAlternate() {
        return strDrinkAlternate;
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

    public String getStrTags() {
        return strTags;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public String getStrSource() {
        return strSource;
    }

    public String getStrImageSource() {
        return strImageSource;
    }

    public String getStrCreativeCommonsConfirmed() {
        return strCreativeCommonsConfirmed;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getStrIngredient1() {
        return strIngredient1;
    }

    public void setStrIngredient1(String strIngredient1) {
        this.strIngredient1 = strIngredient1;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getMealDate() {
        return mealDate;
    }

    public void setMealDate(String mealDate) {
        this.mealDate = mealDate;
    }
}
