package com.example.mealapp.feature.meal_details.presenter;


import com.example.mealapp.feature.meal_details.view.IMealDetails;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.common_layer.models.Ingredient;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;

import java.util.ArrayList;
import java.util.List;

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
                        _repo.deleteMealIngredient(meal.getIdMeal());
                    }
                });
                _view.updateFavoriteIcon(false);
                _view.showToast("Meal removed from your favorites");
            } else {
                _repo.saveFavoriteMeal(new FavoriteMeal(meal));
                List<MealIngredient> ingredients = createFavoriteMealIngredients(meal);
                for (MealIngredient ingredient : ingredients) {
                    _repo.saveFavoriteMealIngredient(ingredient);
                }
                _view.updateFavoriteIcon(true);
                _view.showToast("Meal added to your favorites");
            }
            isProcessingFavoriteToggle = false;
        });
    }

    @Override
    public void toggleMealPlan(DetailedMeal meal) {
        if (isProcessingFavoriteToggle) return;
        isProcessingFavoriteToggle = true;

        String userId = UserSessionHolder.getInstance().getUser().getUid();
        _repo.isMealPlan(meal.getIdMeal(), userId, isPlan -> {
            if (isPlan) {
                _repo.deleteMealPlan(new MealPlan(meal, meal.getMealDate(), meal.getMealType()));
                _repo.isMealFavorite(userId, meal.getIdMeal(), isFavorite -> {
                    if (isFavorite) {
                        _repo.deleteMealIngredient(meal.getIdMeal());
                    }
                });
                _view.updateAddPlanBtnText(false);
                _view.showToast("Meal removed from your plan");
            } else {
                _repo.saveMealPlan(new MealPlan(meal, meal.getMealDate(), meal.getMealType()));
                List<MealIngredient> ingredients = createFavoriteMealIngredients(meal);
                for (MealIngredient ingredient : ingredients) {
                    _repo.saveFavoriteMealIngredient(ingredient);
                }
                _view.updateAddPlanBtnText(true);
                _view.showToast("Meal added to your plan");
            }
            isProcessingFavoriteToggle = false;
        });
    }

    private List<MealIngredient> createFavoriteMealIngredients(DetailedMeal detailedMeal) {
        List<Ingredient> ingredientList = detailedMeal.getIngredients();
        List<MealIngredient> mealIngredients = new ArrayList<>();

        for (Ingredient ingredient : ingredientList) {
            mealIngredients.add(new MealIngredient(
                    detailedMeal.getIdMeal(),
                    ingredient
            ));
        }

        return mealIngredients;
    }
}
