package com.example.mealapp.feature.food_planner_preview.view;

import android.content.Intent;
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

import com.example.mealapp.R;
import com.example.mealapp.feature.food_planner_preview.presenter.FoodPlannerPreviewPresenter;
import com.example.mealapp.feature.food_planner_preview.presenter.IFoodPlannerPreviewPresenter;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;

import java.util.ArrayList;
import java.util.List;


public class FoodPlannerPreviewFragment extends Fragment implements IFoodPlannerPreview, OnPlannedMealClick{

    IFoodPlannerPreviewPresenter presenter;
    PlannedMealsAdapter plannedMealsAdapter;
    TextView youDontHavePlan;
    TextView youNeedToSignInFirst;
    RecyclerView plannedMealsRecyclerView;




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
        presenter = new FoodPlannerPreviewPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(requireActivity())));
        initUi(view);

        plannedMealsAdapter = new PlannedMealsAdapter(new ArrayList<>(), this);
        plannedMealsRecyclerView.setAdapter(plannedMealsAdapter);
    }

    void initUi(View v){
        youDontHavePlan = v.findViewById(R.id.youDontHavePlan);
        youNeedToSignInFirst = v.findViewById(R.id.youNeedToSigninFirst);
        plannedMealsRecyclerView = v.findViewById(R.id.plannedMealsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        plannedMealsRecyclerView.setLayoutManager(linearLayoutManager);

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
    public void onResume() {
        super.onResume();
        presenter.getPlannedMeals(this);
    }
    @Override
    public void showGuestMsg() {
        youNeedToSignInFirst.setVisibility(View.VISIBLE);
        plannedMealsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void planMealClicked(String mealId) {
        Intent intent = new Intent(requireActivity(), MealDetails.class);
        intent.putExtra("PLAN_MEAL_ID", mealId);
        startActivity(intent);
    }
}




/*
    @Override
    public void onClick(Product product) {
        favouritePresenter.deleteFromFav(product);
    }

 */
