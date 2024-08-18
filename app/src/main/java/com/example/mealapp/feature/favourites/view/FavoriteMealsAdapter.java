package com.example.mealapp.feature.favourites.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealapp.R;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;

import java.util.List;

public class FavoriteMealsAdapter extends RecyclerView.Adapter<FavoriteMealsAdapter.FavoriteMealViewHolder> {

    private final List<FavoriteMeal> favoriteMeals;

    private final OnFavoriteMealClick listener;
    public FavoriteMealsAdapter(List<FavoriteMeal> favoriteMeals, OnFavoriteMealClick onFavoriteMealClick) {
        this.favoriteMeals = favoriteMeals;
        this.listener = onFavoriteMealClick;
    }

    public void updateData(List<FavoriteMeal> newFavoriteMeals) {
        favoriteMeals.clear();
        favoriteMeals.addAll(newFavoriteMeals);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new FavoriteMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMealViewHolder holder, int position) {
        FavoriteMeal favoriteMeal = favoriteMeals.get(position);
        holder.mealName.setText(favoriteMeal.getStrMeal());
        Glide.with(holder.itemView.getContext())
                .load(favoriteMeal.getStrMealThumb())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mealImage);

        holder.itemView.setOnClickListener(v -> listener.favoriteMealClicked(favoriteMeal.getIdMeal()));

    }

    @Override
    public int getItemCount() {
        return favoriteMeals.size();
    }

    public static class FavoriteMealViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImage;
        TextView mealName;

        FavoriteMealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
        }
    }
}
