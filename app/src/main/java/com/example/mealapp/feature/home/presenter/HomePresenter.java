package com.example.mealapp.feature.home.presenter;

import android.util.Log;


import com.example.mealapp.feature.home.view.IHome;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.firebase.FirebaseManager;
import com.example.mealapp.utils.firebase.OnUserRetrieveData;
import com.example.mealapp.utils.network.HomeNetworkDelegate;

import java.util.List;
import java.util.Objects;


public class HomePresenter implements IHomePresenter, HomeNetworkDelegate {

    private final IHome _view;
    private final MealRepository _repo;

    private static final String TAG = "HomePresenter";

    public HomePresenter(IHome view, MealRepository repo) {
        _view = view;
        _repo = repo;
    }

    @Override
    public void getDataFromFirebase() {
            String userId = UserSessionHolder.getInstance().getUser().getUid(); // Replace with the actual user ID
            // Fetch Favorite Meals
            FirebaseManager.getInstance().getFavoriteMeals(userId, task -> {
                if (task.isSuccessful()) {
                    List<FavoriteMeal> favoriteMeals = task.getResult();
                    Log.i("firebase", favoriteMeals.size()+"Favorite" );
                    Log.i("firebase", favoriteMeals.get(0).toString());
                    _repo.addFavoriteMeals(favoriteMeals);
                } else {
                    Log.e("error", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            });

            // Fetch Meal Plans
            FirebaseManager.getInstance().getMealPlans(userId, task -> {
                if (task.isSuccessful()) {
                    List<MealPlan> mealPlans = task.getResult();
                    Log.i("firebase", mealPlans.size()+"plan" );
                    Log.i("firebase", mealPlans.get(0).toString());

                    _repo.addMealPlans(mealPlans);
                } else {
                    Log.e("error", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            });

            // Fetch Meal Ingredients
            FirebaseManager.getInstance().getMealIngredients(userId, task -> {
                if (task.isSuccessful()) {
                    List<MealIngredient> ingredients = task.getResult();
                    Log.i("firebase", ingredients.size()+"ingredient" );
                    Log.i("firebase", ingredients.get(0).toString());

                    _repo.addMealIngredients(ingredients);
                } else {
                    Log.e("error", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            });
        }



    @Override
    public void getRandomMeal() {
        _repo.getRandomMeal(this);
    }

    @Override
    public void getRandomMeals() {
        int MAX_MEALS = 20;
        for(int i = 0; i < MAX_MEALS; i++) {
          _repo.getRandomMeals(this);
        }
    }


    @Override
    public void getCurrentUser() {
        FirebaseManager.getInstance().getCurrentUser(
                new OnUserRetrieveData() {
                    public void onUserDataRetrieved(User user) {
                        if (user != null) {
                            _view.getCurrentUserSuccessfully(user);
                        } else {
                            Log.i(TAG, "User is null");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i(TAG, "Error getting user data: " + e.getMessage());
                    }
                });
    }

    @Override
    public void signOut() {
        FirebaseManager.getInstance().signOut();
    }
    @Override
    public void onGetRandomMealSuccessResult(PreviewMeal previewMeal) {
        _view.showRandomMeal(previewMeal);
    }

    @Override
    public void onGetRandomMealsSuccessResult(PreviewMeal meal) {
            _view.showRandomMeals(meal);
    }

    @Override
    public void onFailureResult(String errorMsg) {
        _view.onFailureResult(errorMsg);

    }
}
