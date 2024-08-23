package com.example.mealapp.feature.meal_details.presenter;


import android.util.Log;

import com.example.mealapp.R;
import com.example.mealapp.feature.meal_details.view.IMealDetails;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.common_layer.models.Ingredient;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MealDetailsPresenter implements IMealDetailsPresenter, MealDetailsNetworkDelegate {
    private final IMealDetails _view;
    private final MealRepository _repo;
    private boolean isProcessingFavoriteToggle = false;

    public MealDetailsPresenter(IMealDetails view, MealRepository repo) {
        this._view = view;
        this._repo = repo;
    }

    @Override
    public void getMealDetails(String mealId) {
        _repo.getMealDetails(this, mealId);
    }

    @Override
    public void getFavoriteMeal(String mealId) {
        String userId = UserSessionHolder.getInstance().getUser().getUid();

        _repo.getFavoriteMeal(userId, mealId).observeForever(favoriteMeal -> {
            if (favoriteMeal != null) {
                _repo.getIngredientsForMeal(mealId).observeForever(favoriteMealIngredients -> {
                    DetailedMeal detailedMeal = new DetailedMeal(favoriteMeal, favoriteMealIngredients);
                    _repo.isMealPlan(mealId, userId, isInPlan -> _view.setUpMealDetails(detailedMeal, true, isInPlan));
                });
            }
        });
    }

    @Override
    public void getMealPlan(String mealId) {
        String userId = UserSessionHolder.getInstance().getUser().getUid();

        _repo.getMealPlan(userId, mealId).observeForever(planMeal -> {
            if (planMeal != null) {
                _repo.getIngredientsForMeal(mealId).observeForever(favoriteMealIngredients -> {
                    DetailedMeal detailedMeal = new DetailedMeal(planMeal, favoriteMealIngredients);
                    _repo.isMealFavorite(mealId, userId, isFavorite -> _view.setUpMealDetails(detailedMeal, isFavorite, true));
                });
            }
        });
    }

    @Override
    public void onGetMealDetailsSuccessResult(DetailedMeal meal) {
        boolean isGuest = UserSessionHolder.isGuest();
        if (isGuest) {
            _view.setUpMealDetails(meal, false, false);
        } else {
            String userId = UserSessionHolder.getInstance().getUser().getUid();

            _repo.isMealFavorite(meal.getIdMeal(), userId, isFavorite -> _repo.isMealPlan(meal.getIdMeal(), userId, isInPlan -> _view.setUpMealDetails(meal, isFavorite, isInPlan)));
        }
    }

    @Override
    public void onFailureResult(String errorMsg) {
        _view.onFailureResult(errorMsg);
    }

    @Override
    public void toggleFavoriteStatus(DetailedMeal meal) {
        if (isProcessingFavoriteToggle) return; // Prevent further clicks
        isProcessingFavoriteToggle = true;

        String userId = UserSessionHolder.getInstance().getUser().getUid();
        _repo.isMealFavorite(meal.getIdMeal(), userId, isFavorite -> {
            if (isFavorite) {
                _repo.deleteFavoriteMeal(new FavoriteMeal(meal));
                _repo.isMealPlan(userId, meal.getIdMeal(), isInPlan -> {
                    if (isInPlan) {
                        _repo.deleteMealIngredient(meal.getIdMeal(), userId);
                    }
                });
                _view.updateFavoriteIcon(false);
                _view.showToast(_view.getStringFromRes(R.string.meal_removed_from_favorites));
            } else {
                _repo.saveFavoriteMeal(new FavoriteMeal(meal));
                List<MealIngredient> ingredients = createFavoriteMealIngredients(meal);
                for (MealIngredient ingredient : ingredients) {
                    _repo.saveFavoriteMealIngredient(ingredient);
                }
                _view.updateFavoriteIcon(true);
                _view.showToast(_view.getStringFromRes(R.string.meal_added_to_favorites));
            }
            isProcessingFavoriteToggle = false;
        });
    }

    @Override
    public void saveMealPlan(DetailedMeal meal) {
        String userId = UserSessionHolder.getInstance().getUser().getUid();
        String dateCreated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        _repo.getMealPlanCount(userId, meal.getIdMeal(), meal.getMealDate(), count -> {
            Log.i("MealDetailsPresenter", "Meal plan exists three times: " + count);
            if (count >= 3) {
                _view.showWarningMealCannotBeAdded();
            } else {
                _repo.saveMealPlan(new MealPlan(meal, meal.getMealDate(), meal.getMealType(), dateCreated));
                List<MealIngredient> ingredients = createFavoriteMealIngredients(meal);
                for (MealIngredient ingredient : ingredients) {
                    _repo.saveFavoriteMealIngredient(ingredient);
                }
                _view.showToast(_view.getStringFromRes(R.string.meal_added_to_plan));
            }
        });
    }

    @Override
    public String getCurrentLang(){
        return _repo.getPreference(ConstantKeys.LANGUAGE_KEY, false);
    }

    @Override
    public void checkPlanExist(DetailedMeal meal){
        String userId = UserSessionHolder.getInstance().getUser().getUid();
        _repo.getMealPlanCount(userId, meal.getIdMeal(),meal.getMealDate(), count -> {
            Log.i("MealDetailsPresenter", "Meal plan exists on the same day: " + count);
            if (count > 0 && count < 3) {
                Log.i("MealDetailsPresenter", "Meal plan already exists on the same day");
                _view.showWarningMealPlanExist(meal);
            }else{
                saveMealPlan(meal);
            }
        });
    }

    private List<MealIngredient> createFavoriteMealIngredients(DetailedMeal detailedMeal) {
        List<Ingredient> ingredientList = detailedMeal.getIngredients();
        List<MealIngredient> mealIngredients = new ArrayList<>();

        for (Ingredient ingredient : ingredientList) {
            mealIngredients.add(new MealIngredient(detailedMeal.getIdMeal(), ingredient));
        }

        return mealIngredients;
    }
}
