package com.example.mealapp.feature.food_planner_preview.presenter;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.mealapp.R;
import com.example.mealapp.feature.food_planner_preview.view.IFoodPlannerPreview;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FoodPlannerPreviewPresenter implements IFoodPlannerPreviewPresenter {

    private final IFoodPlannerPreview _view;
    private final MealRepository _repo;
    private List<MealPlan> _plannedMeals;

    public FoodPlannerPreviewPresenter(IFoodPlannerPreview view, MealRepository repo) {
        _view = view;
        _repo = repo;
    }

    private String formatDate(CalendarDay date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth() - 1, date.getDay());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH);
        return simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public void filterMealsByDate(CalendarDay date) {
        String formattedDate = formatDate(date);
        List<MealPlan> filteredMeals = new ArrayList<>();
        for (MealPlan meal : _plannedMeals) {
            if (meal.getDate().equals(formattedDate)) {
                filteredMeals.add(meal);
            }
        }
        _view.filterMealsByDate(filteredMeals);
    }

    @Override
    public void getPlannedMeals(LifecycleOwner owner) {
        boolean isGuest = UserSessionHolder.isGuest();
            if (isGuest) {
                _view.showGuestMsg();
            } else {
                Log.i("TAG", "getPlannedMeals:ssss");
                String userId = UserSessionHolder.getInstance().getUser().getUid();
                _repo.getAllMealPlansForUser(userId).observe(owner, meals -> {
                    _plannedMeals = meals;
                    _view.showPlannedMeals(meals);
                    _view.updateCalendarWithMealDates(getMealDates(meals));
                });
            }
        }

    @Override
    public void deleteMealPlan(MealPlan meal) {
        String userId = UserSessionHolder.getInstance().getUser().getUid();
        _repo.deleteMealPlan(meal);
        _repo.isMealFavorite(userId, meal.getIdMeal(), isFavorite -> {
            if (isFavorite) {
                _repo.deleteMealIngredient(meal.getIdMeal(), userId);
            }
            _view.showToast(_view.getStringFromRes(R.string.meal_removed_from_plan));
        });
        _view.onMealPlanDeleted();
    }

    private Set<CalendarDay> getMealDates(List<MealPlan> _plannedMeals) {
        Set<CalendarDay> dates = new HashSet<>();
        for (MealPlan meal : _plannedMeals) {
            String mealDate = meal.getDate();
            CalendarDay calendarDay = parseDate(mealDate);

            if (calendarDay != null) {
                dates.add(calendarDay);
            }
        }
        return dates;
    }

    private CalendarDay parseDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH);
        try {
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            Log.e("TAG", "Error parsing date: " + e.getMessage());
            return null;
        }
    }
}
