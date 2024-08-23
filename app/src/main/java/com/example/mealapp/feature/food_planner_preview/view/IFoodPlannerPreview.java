package com.example.mealapp.feature.food_planner_preview.view;

import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;
import java.util.Set;

public interface IFoodPlannerPreview {

    void showPlannedMeals(List<MealPlan> meals);

    void showGuestMsg();

     void filterMealsByDate(List<MealPlan> meals);
     void updateCalendarWithMealDates(Set<CalendarDay> mealDates);


    String getStringFromRes(int resId);
    void showToast(String message);

    void onMealPlanDeleted();
}



