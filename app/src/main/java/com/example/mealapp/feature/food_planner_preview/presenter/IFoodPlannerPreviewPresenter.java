package com.example.mealapp.feature.food_planner_preview.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public interface IFoodPlannerPreviewPresenter {

     void getPlannedMeals(LifecycleOwner owner);
     void filterMealsByDate(CalendarDay date);

     void deleteMealPlan(MealPlan meal);


}
