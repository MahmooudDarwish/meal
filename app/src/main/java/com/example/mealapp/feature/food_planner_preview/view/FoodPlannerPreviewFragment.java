package com.example.mealapp.feature.food_planner_preview.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.R;
import com.example.mealapp.feature.food_planner_preview.model.MealEventDecorator;
import com.example.mealapp.feature.food_planner_preview.presenter.FoodPlannerPreviewPresenter;
import com.example.mealapp.feature.food_planner_preview.presenter.IFoodPlannerPreviewPresenter;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
import com.example.mealapp.utils.shared_preferences.SharedPreferencesManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FoodPlannerPreviewFragment extends Fragment implements IFoodPlannerPreview, OnPlannedMealAction {

    IFoodPlannerPreviewPresenter presenter;
    PlannedMealsAdapter plannedMealsAdapter;
    TextView youDontHavePlan;
    TextView youNeedToSignInFirst;
    RecyclerView plannedMealsRecyclerView;
    MaterialCalendarView calenderFilter;
    private CalendarDay selectedDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_planned_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new FoodPlannerPreviewPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(requireActivity()), SharedPreferencesManager.getInstance(requireActivity())));
        initUi(view);
        setUpListeners();
    }

    private void setUpListeners() {
        calenderFilter.setOnDateChangedListener((widget, date, selected) -> {
            if (selectedDate != null && selectedDate.equals(date)) {
                calenderFilter.clearSelection();
                selectedDate = null;
                presenter.getPlannedMeals(this);
            } else {
                selectedDate = date;
                presenter.filterMealsByDate(date);
            }
        });
    }


    @Override
    public void updateCalendarWithMealDates(Set<CalendarDay> mealDates) {
        int dotColor = Color.RED;
        MealEventDecorator decorator = new MealEventDecorator(mealDates, dotColor);
        calenderFilter.addDecorator(decorator);
    }

    @Override
    public String getStringFromRes(int resId) {
        return getString(resId);
    }

    @Override
    public void showToast(String message) {
        requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onMealPlanDeleted() {
        presenter.getPlannedMeals(this);
    }


    void initUi(View v){
        youDontHavePlan = v.findViewById(R.id.youDontHavePlan);
        calenderFilter = v.findViewById(R.id.calenderFilter);
        youNeedToSignInFirst = v.findViewById(R.id.youNeedToSigninFirst);
        plannedMealsRecyclerView = v.findViewById(R.id.plannedMealsRecyclerView);
        plannedMealsAdapter = new PlannedMealsAdapter(new ArrayList<>(), this);
        plannedMealsRecyclerView.setAdapter(plannedMealsAdapter);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            plannedMealsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        }else{
            plannedMealsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

        }
    }
    @Override
    public void showPlannedMeals(List<MealPlan> meals) {
        if (meals.isEmpty()){
            youDontHavePlan.setVisibility(View.VISIBLE);
            plannedMealsRecyclerView.setVisibility(View.GONE);
        }else{

            youDontHavePlan.setVisibility(View.GONE);
            plannedMealsRecyclerView.setVisibility(View.VISIBLE);
            plannedMealsAdapter.updateData(meals);
        }
    }

    @Override
    public void filterMealsByDate(List<MealPlan> meals){
        plannedMealsAdapter.updateData(meals);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getPlannedMeals(this);
    }
    @Override
    public void showGuestMsg() {
        youNeedToSignInFirst.setVisibility(View.VISIBLE);
        calenderFilter.setVisibility(View.GONE);
        plannedMealsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void planMealClicked(String mealId) {
        Intent intent = new Intent(requireActivity(), MealDetails.class);
        intent.putExtra(ConstantKeys.PLAN_MEAL_ID, mealId);
        startActivity(intent);
    }

    @Override
    public void deletePlanMealClicked(MealPlan mealPlan) {
            presenter.deleteMealPlan(mealPlan);
    }
}
