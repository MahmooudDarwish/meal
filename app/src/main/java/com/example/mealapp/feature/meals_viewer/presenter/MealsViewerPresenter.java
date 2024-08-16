package com.example.mealapp.feature.meals_viewer.presenter;

import com.example.mealapp.feature.meals_viewer.model.MealsViewModel;
import com.example.mealapp.feature.meals_viewer.view.IMealsViewer;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;

import java.util.ArrayList;
import java.util.List;

public class MealsViewerPresenter implements IMealsViewerPresenter {

    private final IMealsViewer view;
    private final MealsViewModel viewModel;

    public MealsViewerPresenter(IMealsViewer view) {
        this.view = view;
        this.viewModel = MealsViewModel.getInstance();
    }

    @Override
    public void onSearchQuery(String query) {
        if (query.isEmpty()) {
            view.displayMeals(viewModel.getMeals());
            return;
        }

        List<PreviewMeal> filteredMeals = new ArrayList<>();
        for (PreviewMeal meal : viewModel.getMeals()) {
            if (meal.getStrMeal().toLowerCase().contains(query.toLowerCase())) {
                filteredMeals.add(meal);
            }
        }

        if (!filteredMeals.isEmpty()) {
            view.displayMeals(filteredMeals);
        }
    }

    @Override
    public void loadMeals() {
        List<PreviewMeal> meals = viewModel.getMeals();
        if (meals != null && !meals.isEmpty()) {
            view.displayMeals(meals);
        }
    }
}
