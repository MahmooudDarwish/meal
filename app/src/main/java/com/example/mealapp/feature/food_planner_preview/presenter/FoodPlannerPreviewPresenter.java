package com.example.mealapp.feature.food_planner_preview.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.example.mealapp.feature.food_planner_preview.view.IFoodPlannerPreview;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.data_source_manager.MealRepository;

public class FoodPlannerPreviewPresenter implements  IFoodPlannerPreviewPresenter{

    private final IFoodPlannerPreview _view;
    private final MealRepository _repo;

    public FoodPlannerPreviewPresenter(IFoodPlannerPreview view, MealRepository repo) {
        _view = view;
        _repo = repo;

    }

    @Override
    public void getPlannedMeals(LifecycleOwner owner) {
        boolean isGuest = UserSessionHolder.isGuest();
        if (isGuest) {
            _view.showGuestMsg();
        } else {
            String userId = UserSessionHolder.getInstance().getUser().getUid();
            _repo.getAllMealPlansForUser(userId).observe(owner, _view::showPlannedMeals);
        }
    }
}
