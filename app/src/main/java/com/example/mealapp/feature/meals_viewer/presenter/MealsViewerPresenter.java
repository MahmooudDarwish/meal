package com.example.mealapp.feature.meals_viewer.presenter;

import com.example.mealapp.feature.meals_viewer.model.MealsViewModel;
import com.example.mealapp.feature.meals_viewer.view.IMealsViewer;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepository;

import java.util.ArrayList;
import java.util.List;

public class MealsViewerPresenter implements IMealsViewerPresenter {

    private final IMealsViewer view;
    private final MealsViewModel viewModel;

    private final MealRepository _repo;

    public MealsViewerPresenter(IMealsViewer view, MealRepository _repo) {
        this.view = view;
        this.viewModel = MealsViewModel.getInstance();
        this._repo = _repo;
    }

    @Override
    public void onSearchQuery(String query) {
        if (query.isEmpty()) {
            view.displayMeals(viewModel.getMeals());
            return;
        }

        String searchLower = query.toLowerCase();
        List<PreviewMeal> exactMatchMeals = new ArrayList<>();
        List<PreviewMeal> partialMatchMeals = new ArrayList<>();

        for (PreviewMeal meal : viewModel.getMeals()) {
            String mealNameLower = meal.getStrMeal().toLowerCase();

            if (mealNameLower.startsWith(searchLower)) {
                exactMatchMeals.add(meal);
            } else if (mealNameLower.contains(searchLower)) {
                partialMatchMeals.add(meal);
            }
        }

        List<PreviewMeal> filteredMeals = new ArrayList<>(exactMatchMeals);
        filteredMeals.addAll(partialMatchMeals);

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

    @Override
    public String getCurrentLang(){
        return _repo.getPreference(ConstantKeys.LANGUAGE_KEY, false);
    }
}
