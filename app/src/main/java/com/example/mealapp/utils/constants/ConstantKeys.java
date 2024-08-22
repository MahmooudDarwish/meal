package com.example.mealapp.utils.constants;

public abstract class ConstantKeys {
    public static final String USER_DATA = "user_data";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String STAY_LOGGED_IN = "stay_logged_in";
    public static final String FAVORITE_MEAL_ID = "FAVORITE_MEAL_ID";
    public static final String PLAN_MEAL_ID = "PLAN_MEAL_ID";
    public static final String MEAL_ID = "MEAL_ID";
    public static final String KEY_ID_MEAL = "idMeal";
    public static final String KEY_ID_USER = "idUser";
    public static final String KEY_STR_MEAL = "strMeal";
    public static final String KEY_STR_CATEGORY = "strCategory";
    public static final String KEY_STR_AREA = "strArea";
    public static final String LANGUAGE_KEY = "language";
    public static final String USER_SETTINGS = "user_settings";

    public static final String KEY_STR_INSTRUCTIONS = "strInstructions";
    public static final String KEY_STR_MEAL_THUMB = "strMealThumb";
    public static final String KEY_STR_YOUTUBE = "strYoutube";
    public static final String KEY_MEAL_TYPE = "mealType";
    public static final String KEY_DATE = "date";

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";


    public static final String KEY_INGREDIENT_NAME = "ingredientName";
    public static final String KEY_INGREDIENT_IMAGE = "ingredientImage";
    public static final String KEY_MEASURE = "measure";


    public static final String BASE_INGREDIENT_IMAGE_URL = "https://www.themealdb.com/images/ingredients/";
    public static final String INGREDIENT_IMAGE_SUFFIX = "-Small.png";

    public static String getIngredientImageUrl(String ingredientName) {
        return BASE_INGREDIENT_IMAGE_URL + ingredientName + INGREDIENT_IMAGE_SUFFIX;
    }

    public static final String DATABASE_NAME = "app_database";

    public static final String COLLECTION_FAVORITE_MEALS = "favorite_meals";
    public static final String COLLECTION_PLAN_MEALS = "plan_meals";
    public static final String COLLECTION_INGREDIENTS = "ingredients";


    public static final String COLLECTION_USERS = "users";

    public static final String KEY_AR = "ar";
    public static final String KEY_EN = "en";






}
