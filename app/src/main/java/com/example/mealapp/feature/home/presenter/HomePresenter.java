package com.example.mealapp.feature.home.presenter;

import com.example.mealapp.feature.home.view.IHome;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.network.NetworkDelegate;

public class HomePresenter implements IHomePresenter, NetworkDelegate {

    private final IHome _view;
    private final MealRepository _repo;

    public HomePresenter(IHome view, MealRepository repo) {
        _view = view;
        _repo = repo;
    }
    @Override
    public void getRandomMeal() {
        _repo.getRandomMeal(this);
    }

    @Override
    public void onSuccessResult(PreviewMeal previewMeal) {
        _view.showRandomMeal(previewMeal);

    }

    @Override
    public void onFailureResult(String errorMsg) {
        _view.onFailureResult(errorMsg);

    }
}
