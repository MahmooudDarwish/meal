package com.example.mealapp.feature.settings.presenter;


import androidx.lifecycle.LifecycleOwner;

import com.example.mealapp.R;
import com.example.mealapp.feature.settings.view.ISettings;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepository;

import android.os.Handler;

public class SettingsPresenter implements ISettingsPresenter {

    private final ISettings view;
    private final MealRepository repo;
    private final Handler handler = new Handler();
    private final int checkInterval = 1000;

    public SettingsPresenter(ISettings view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void signOut() {
        UserSessionHolder.getInstance().setUser(null);
        repo.clearPreferences(true);
        repo.signOut();
        view.onSignOut();
    }
    public void uploadDataToFirebase(LifecycleOwner owner) {
        startNetworkChecking();

        String userId = UserSessionHolder.getInstance().getUser().getUid();

        repo.getAllFavoriteMealsForUser(userId).observe(owner, favoriteMeals -> {
            if (favoriteMeals != null && !favoriteMeals.isEmpty()) {
                view.showLoading();
                repo.setFavoriteMeals(favoriteMeals, task -> {
                    if (task.isSuccessful()) {
                        view.showMessage(view.getStringFromRes(R.string.favorite_meals_uploaded_successfully));
                    } else {
                        view.showMessage(view.getStringFromRes(R.string.failed_to_upload_favorite_meals));
                    }
                });
            } else {
                view.showMessage(view.getStringFromRes(R.string.no_favorite_meals_found));
            }
        });

        repo.getAllMealPlansForUser(userId).observe(owner, mealPlans -> {
            if (mealPlans != null && !mealPlans.isEmpty()) {
                view.showLoading();
                repo.setMealPlans(mealPlans, task -> {
                    if (task.isSuccessful()) {
                        view.showMessage(view.getStringFromRes(R.string.meal_plans_uploaded_successfully));
                    } else {
                        view.showMessage(view.getStringFromRes(R.string.failed_to_upload_meal_plans));
                    }
                });
            } else {
                view.showMessage(view.getStringFromRes(R.string.no_meal_plans_found));
            }
        });

        repo.getIngredientsForUser(userId).observe(owner, mealIngredients -> {
            if (mealIngredients != null && !mealIngredients.isEmpty()) {
                view.showLoading();
                repo.setMealIngredients(mealIngredients, task -> {
                    if (task.isSuccessful()) {
                        view.showMessage(view.getStringFromRes(R.string.meal_ingredients_uploaded_successfully));
                    } else {
                        view.showMessage(view.getStringFromRes(R.string.failed_to_upload_meal_ingredients));
                    }
                    cancelChecking();
                    view.hideLoading();
                });
            }
        });
        cancelChecking();
        view.hideLoading();

    }

    @Override
    public void handleLanguageSetting(boolean isEnglish) {
        String languageCode = isEnglish ? ConstantKeys.KEY_EN : ConstantKeys.KEY_AR;
        view.setLocale(languageCode);
        repo.savePreference(ConstantKeys.LANGUAGE_KEY, languageCode, false);
    }

    @Override
    public String getCurrentLang(){
        return repo.getPreference(ConstantKeys.LANGUAGE_KEY, false);
    }


    @Override
    public void initializeLanguageSetting() {
        String savedLanguage = repo.getPreference(ConstantKeys.LANGUAGE_KEY, false);
        boolean isEnglish = savedLanguage != null && savedLanguage.equals(ConstantKeys.KEY_EN);
        view.setLanguage(isEnglish);
    }

    private void startNetworkChecking() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NetworkUtil.isConnected()) {
                    cancelChecking();
                    view.showBackUpWarning();
                } else  {
                    handler.postDelayed(this, checkInterval); // Continue checking
                }
            }
        }, checkInterval);
    }


    private void cancelChecking() {
        view.hideLoading();
        handler.removeCallbacksAndMessages(null); // Stop network checking
    }
}
