package com.example.mealapp.feature.meals_viewer.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private final List<PreviewMeal> meals;

    OnMealItemClicked listener;

    public MealsAdapter(List<PreviewMeal> meals , OnMealItemClicked listener) {
        this.meals = meals;
        this.listener = listener;
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
