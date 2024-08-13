package com.example.mealapp.feature.meal_details.presenter;

import com.example.mealapp.feature.meal_details.view.IMealDetails;
import com.example.mealapp.utils.common_layer.models.DetailedMeal;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.network.MealDetailsNetworkDelegate;

public class MealDetailsPresenter implements IMealDetailsPresenter, MealDetailsNetworkDelegate {
    private final IMealDetails _view;
    private final MealRepository _repo;

    public MealDetailsPresenter(IMealDetails view, MealRepository repo) {
        this._view = view;
        this._repo = repo;
    }

    @Override
    public void getMealDetails(String mealId) {
        _repo.getMealDetails(this, mealId);
    }

    @Override
    public void onGetMealDetailsSuccessResult(DetailedMeal meal) {
        _view.setUpMealDetails(meal);
    }

    @Override
    public void onFailureResult(String errorMsg) {
        _view.onFailureResult(errorMsg);
    }
}
