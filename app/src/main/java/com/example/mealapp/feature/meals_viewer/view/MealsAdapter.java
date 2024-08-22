package com.example.mealapp.feature.meals_viewer.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    private List<PreviewMeal> meals;

    OnMealItemClicked listener;

    public MealsAdapter(List<PreviewMeal> meals , OnMealItemClicked listener) {
        this.meals = meals;
        this.listener = listener;
    }

    public void updateMeals(List<PreviewMeal> newMeals) {
        this.meals = newMeals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Animation fadeIn = AnimationUtils.loadAnimation(holder.mealImage.getContext(), R.anim.fade_in);
        Animation slideOutBottom = AnimationUtils.loadAnimation(holder.mealName.getContext(), R.anim.slide_in_bottom);
        holder.mealImage.startAnimation(fadeIn);
        holder.mealName.startAnimation(slideOutBottom);

        PreviewMeal meal = meals.get(position);
        holder.mealName.setText(meal.getStrMeal());

        Glide.with(holder.itemView.getContext()).
                load(meal.getStrMealThumb()).into(holder.mealImage);

        holder.itemView.setOnClickListener( v -> listener.onMealItemClick(meal.getIdMeal()));

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage;
        TextView mealName;
        CardView mealCard;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
            mealCard = itemView.findViewById(R.id.mealCard);
        }
    }
}
