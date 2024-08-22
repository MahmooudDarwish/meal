package com.example.mealapp.feature.food_planner_preview.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealapp.R;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;

import java.util.List;

public class PlannedMealsAdapter extends RecyclerView.Adapter<PlannedMealsAdapter.PlannedMealViewHolder> {

    private final List<MealPlan> plannedMeals;

    private final OnPlannedMealClick listener;
    public PlannedMealsAdapter(List<MealPlan> favoriteMeals, OnPlannedMealClick onPlannedMealClick) {
        this.plannedMeals = favoriteMeals;
        this.listener = onPlannedMealClick;
    }

    public void updateData(List<MealPlan> newPlannedMeals) {
        plannedMeals.clear();
        plannedMeals.addAll(newPlannedMeals);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlannedMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_meal, parent, false);
        return new PlannedMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannedMealViewHolder holder, int position) {

        Animation fadeIn = AnimationUtils.loadAnimation(holder.mealImage.getContext(), R.anim.fade_in);
        Animation slideOutBottom = AnimationUtils.loadAnimation(holder.mealName.getContext(), R.anim.slide_in_bottom);
        Animation slideOutTop = AnimationUtils.loadAnimation(holder.mealName.getContext(), R.anim.slide_in_right);

        holder.mealImage.startAnimation(fadeIn);
        holder.mealName.startAnimation(slideOutBottom);
        holder.mealPlanDate.startAnimation(slideOutTop);
        holder.mealType.startAnimation(slideOutTop);

        MealPlan mealPlan = plannedMeals.get(position);
        holder.mealName.setText(mealPlan.getStrMeal());
        holder.mealPlanDate.setText(mealPlan.getDate());
        holder.mealType.setText(mealPlan.getMealType());

        Glide.with(holder.itemView.getContext())
                .load(mealPlan.getStrMealThumb())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mealImage);

        holder.itemView.setOnClickListener(v -> listener.planMealClicked(mealPlan.getIdMeal()));

    }

    @Override
    public int getItemCount() {
        return plannedMeals.size();
    }

    public static class PlannedMealViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImage;
        TextView mealName;

        TextView mealPlanDate;
        TextView mealType;

        PlannedMealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
            mealPlanDate = itemView.findViewById(R.id.mealPlanDate);
            mealType = itemView.findViewById(R.id.mealType);
        }
    }
}
