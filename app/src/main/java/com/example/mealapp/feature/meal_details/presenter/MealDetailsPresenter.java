package com.example.mealapp.feature.meal_details.presenter;


import com.example.mealapp.feature.meal_details.view.IMealDetails;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMealIngredient;
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
                    _view.setUpMealDetails(detailedMeal, true);
                });
            }
        });
    }

    @Override
    public void onGetMealDetailsSuccessResult(DetailedMeal meal) {
        boolean isGuest = UserSessionHolder.isGuest();
        if (isGuest) {
            _view.setUpMealDetails(meal, false);
        } else {
            String userId = UserSessionHolder.getInstance().getUser().getUid();
            _repo.isMealFavorite(meal.getIdMeal(), userId, isFavorite -> _view.setUpMealDetails(meal, isFavorite));
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
                _view.onToggleFavouriteMeal("Meal removed from favorites");
                _view.updateFavoriteIcon(false);
            } else {
                _repo.saveFavoriteMeal(new FavoriteMeal(meal));
                List<FavoriteMealIngredient> ingredients = createFavoriteMealIngredients(meal);
                for (FavoriteMealIngredient ingredient : ingredients) {
                    _repo.saveFavoriteMealIngredient(ingredient);
                }
                _view.updateFavoriteIcon(true);
                _view.onToggleFavouriteMeal("Meal Saved to favorites");
            }
            isProcessingFavoriteToggle = false; // Reset after processing
        });
    }

    private List<FavoriteMealIngredient> createFavoriteMealIngredients(DetailedMeal detailedMeal) {
        List<Ingredient> ingredientList = detailedMeal.getIngredients();
        List<FavoriteMealIngredient> favoriteMealIngredients = new ArrayList<>();

        for (Ingredient ingredient : ingredientList) {
            favoriteMealIngredients.add(new FavoriteMealIngredient(
                    detailedMeal.getIdMeal(),
                    ingredient
            ));
        }

        return favoriteMealIngredients;
    }
}
