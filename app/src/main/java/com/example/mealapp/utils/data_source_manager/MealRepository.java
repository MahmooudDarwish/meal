package com.example.mealapp.utils.data_source_manager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.dp.IsFavoriteMealCallback;
import com.example.mealapp.utils.dp.IsPlanMealCallback;
import com.example.mealapp.utils.firebase.OnUserRetrieveData;
import com.example.mealapp.utils.network.HomeNetworkDelegate;
import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;
import com.example.mealapp.utils.network.SearchNetworkDelegate;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import java.util.List;

public interface MealRepository {

     // Remote Data Source Methods
     void getRandomMeal(HomeNetworkDelegate homeNetworkDelegate);

     void getRandomMeals(HomeNetworkDelegate homeNetworkDelegate);

     void getAllCategories(SearchNetworkDelegate searchNetworkDelegate);

     void getAllCountries(SearchNetworkDelegate searchNetworkDelegate);

     void getAllIngredients(SearchNetworkDelegate searchNetworkDelegate);

     void getMealDetails(MealDetailsNetworkDelegate mealDetailsNetworkDelegate, String mealId);

     void getMealsByCategory(SearchNetworkDelegate searchNetworkDelegate, String category);

     void getMealsByCountry(SearchNetworkDelegate searchNetworkDelegate, String country);

     void getMealsByIngredient(SearchNetworkDelegate searchNetworkDelegate, String ingredient);

     // Local Data Source Methods
     void saveFavoriteMeal(FavoriteMeal favoriteMeal);
     void addFavoriteMeals(List<FavoriteMeal> favoriteMeals);

     void deleteFavoriteMeal(FavoriteMeal favoriteMeal);

     LiveData<FavoriteMeal> getFavoriteMeal(String userId, String mealId);

     LiveData<List<FavoriteMeal>> getAllFavoriteMealsForUser(String userId);

     void isMealFavorite(String userId, String mealId, IsFavoriteMealCallback callback);

     void saveMealPlan(MealPlan mealPlan);

     void addMealPlans(List<MealPlan> mealPlans);

     void deleteMealPlan(MealPlan mealPlan);

     void isMealPlan(String userId, String mealId, IsPlanMealCallback callback);


     LiveData<MealPlan> getMealPlan(String userId, String mealId);

     LiveData<List<MealPlan>> getAllMealPlansForUser(String userId);

     void saveFavoriteMealIngredient(MealIngredient ingredient);

     void addMealIngredients(List<MealIngredient> ingredients);

     void deleteMealIngredient(String mealId, String userId);

     LiveData<List<MealIngredient>> getIngredientsForMeal(String mealId);
     LiveData<List<MealIngredient>> getIngredientsForUser(String userId);

     // Authentication methods
     void signInWithGoogle(GoogleSignInAccount account, @NonNull OnCompleteListener<AuthResult> onCompleteListener);

     void signUp(@NonNull String email, @NonNull String password, @NonNull String name, @NonNull OnCompleteListener<AuthResult> onCompleteListener);

     void signIn(@NonNull String email, @NonNull String password, @NonNull OnCompleteListener<AuthResult> onCompleteListener);

     void getCurrentUser(@NonNull OnUserRetrieveData listener);

     void signOut();

     // Data handling methods
     void saveUserData(User user);

     void setFavoriteMeals(List<FavoriteMeal> meals, @NonNull OnCompleteListener<Void> onCompleteListener);

     void setMealPlans(List<MealPlan> plans, @NonNull OnCompleteListener<Void> onCompleteListener);

     void setMealIngredients(List<MealIngredient> ingredients, @NonNull OnCompleteListener<Void> onCompleteListener);

     void getFavoriteMeals(String userId, @NonNull OnCompleteListener<List<FavoriteMeal>> onCompleteListener);

     void getMealPlans(String userId, @NonNull OnCompleteListener<List<MealPlan>> onCompleteListener);

     void getMealIngredients(String userId, @NonNull OnCompleteListener<List<MealIngredient>> onCompleteListener);

     //Shared prefernnces
     void savePreference(String key, String value, boolean fromUserData);
     String getPreference(String key, boolean fromUserData);
     void clearPreferences(boolean fromUserData);
     void clearAllPreferences();

      void savePreference(String key, boolean value, boolean fromUserData);
      boolean getPreference(String key, boolean defaultValue, boolean fromUserData);


     }
