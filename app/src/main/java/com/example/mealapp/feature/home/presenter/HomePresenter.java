package com.example.mealapp.feature.home.presenter;

import android.util.Log;


import com.example.mealapp.feature.home.view.IHome;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.firebase.OnUserRetrieveData;
import com.example.mealapp.utils.network.HomeNetworkDelegate;
import java.util.List;


public class HomePresenter implements IHomePresenter, HomeNetworkDelegate {

    private final IHome _view;
    private final MealRepository repo;


    private static final String TAG = "HomePresenter";

    public HomePresenter(IHome view, MealRepository repo) {
        _view = view;
        this.repo = repo;
    }

    @Override
    public void signOut() {
        UserSessionHolder.getInstance().setUser(null);
        repo.clearPreferences(true);
        repo.signOut();
        _view.onSignOut();
    }

    @Override
    public void checkUser() {
        boolean stayLoggedIn = repo.getPreference(ConstantKeys.STAY_LOGGED_IN, false, true);
        String userNameStr = repo.getPreference(ConstantKeys.USER_NAME, true);
        String userEmail = repo.getPreference(ConstantKeys.USER_EMAIL, true);

        if (stayLoggedIn && UserSessionHolder.isGuest()) {
            UserSessionHolder.getInstance().setUser(new User(userEmail, userNameStr));
            getCurrentUser();
        }

        if (!UserSessionHolder.isGuest()) {
            getDataFromFirebase();
        }
    }

    private void getDataFromFirebase() {
        String userId = UserSessionHolder.getInstance().getUser().getUid();
        repo.getFavoriteMeals(userId, task -> {
            if (task.isSuccessful()) {
                List<FavoriteMeal> favoriteMeals = task.getResult();
                repo.addFavoriteMeals(favoriteMeals);
            }
        });

        repo.getMealPlans(userId, task -> {
            if (task.isSuccessful()) {
                List<MealPlan> mealPlans = task.getResult();
                repo.addMealPlans(mealPlans);
            }
        });

        repo.getMealIngredients(userId, task -> {
            if (task.isSuccessful()) {
                List<MealIngredient> ingredients = task.getResult();
                repo.addMealIngredients(ingredients);
            }
        });
    }


    @Override
    public void getRandomMeal() {
        repo.getRandomMeal(this);
    }

    @Override
    public void getRandomMeals() {
        int MAX_MEALS = 20;
        for (int i = 0; i < MAX_MEALS; i++) {
            repo.getRandomMeals(this);
        }
    }

    public void getCurrentUser() {
        repo.getCurrentUser(new OnUserRetrieveData() {
            public void onUserDataRetrieved(User user) {
                if (user != null) {
                    _view.getCurrentUserSuccessfully(user);
                } else {
                    _view.getCurrentUserFailed();
                    Log.i(TAG, "User is null");
                }
            }

            @Override
            public void onError(Exception e) {
                _view.getCurrentUserFailed();
                Log.i(TAG, "Error getting user data: " + e.getMessage());
            }
        });
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
