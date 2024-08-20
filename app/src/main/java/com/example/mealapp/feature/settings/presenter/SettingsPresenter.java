package com.example.mealapp.feature.settings.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.example.mealapp.feature.settings.view.ISettings;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.firebase.FirebaseManager;

public class SettingsPresenter implements ISettingsPresenter{

    private final ISettings view;
    private final MealRepository repo;
    public SettingsPresenter(ISettings view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void signOut() {
        FirebaseManager.getInstance().signOut();
    }

    public void uploadDataToFirebase(LifecycleOwner owner) {
        String userId = UserSessionHolder.getInstance().getUser().getUid();

        view.showLoading();

        repo.getAllFavoriteMealsForUser(userId).observe(owner, favoriteMeals -> {
            if (favoriteMeals != null && !favoriteMeals.isEmpty()) {
                FirebaseManager.getInstance().setFavoriteMeals(favoriteMeals, task -> {
                    if (task.isSuccessful()) {
                        view.showMessage("Favorite meals uploaded successfully.");
                    } else {
                        view.showMessage("Failed to upload favorite meals.");
                    }
                });
            }
        });

        // Fetch meal plans from Room
        repo.getAllMealPlansForUser(userId).observe(owner, mealPlans -> {
            if (mealPlans != null && !mealPlans.isEmpty()) {
                FirebaseManager.getInstance().setMealPlans(mealPlans, task -> {
                    if (task.isSuccessful()) {
                        view.showMessage("Meal plans uploaded successfully.");
                    } else {
                        view.showMessage("Failed to upload meal plans.");
                    }
                });
            }
        });

        // Fetch meal ingredients from Room
        repo.getIngredientsForUser(userId).observe(owner, mealIngredients -> {
            if (mealIngredients != null && !mealIngredients.isEmpty()) {
                FirebaseManager.getInstance().setMealIngredients(mealIngredients, task -> {
                    if (task.isSuccessful()) {
                        view.showMessage("Meal ingredients uploaded successfully.");
                    } else {
                        view.showMessage("Failed to upload meal ingredients.");
                    }
                    view.hideLoading();
                });
            }
        });
    }

}
