package com.example.mealapp.feature.search.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.utils.common_layer.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>   {

    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<Ingredient> filteredIngredients = new ArrayList<>();
    private final LoadMoreListener loadMoreListener;

    private final OnIngredientClickedListener listener;
    private boolean isLoading = false;

    public IngredientAdapter(LoadMoreListener loadMoreListener, OnIngredientClickedListener listener) {
        this.loadMoreListener = loadMoreListener;
        this.listener = listener;
    }

    public void setIngredients(List<Ingredient> newIngredients){
        ingredients.clear();
        filteredIngredients.clear();
        ingredients.addAll(newIngredients);
        filteredIngredients.addAll(newIngredients);
        notifyDataSetChanged();
    }
    public synchronized void addIngredients(List<Ingredient> newIngredients) {
        Log.d("IngredientAdapter", "Adding ingredients: " + newIngredients.size());
        ingredients.addAll(newIngredients);
        filteredIngredients.addAll(newIngredients);
        notifyDataSetChanged();
        isLoading = false;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = filteredIngredients.get(position);

        String ingredientTitle = ingredient.getStrIngredient();
        holder.ingredientName.setText(ingredientTitle);
        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredientTitle + "-Small.png";
        Glide.with(holder.ingredientImage.getContext())
                .load(imageUrl)
                .into(holder.ingredientImage);
        holder.itemView.setOnClickListener(
                v -> listener.onIngredientClicked(ingredientTitle)
        );
        if (position == getItemCount() - 1 && !isLoading) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
        return filteredIngredients.size();
    }




    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView ingredientImage;
        private final TextView ingredientName;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientName = itemView.findViewById(R.id.ingredientName);
        }
    }
}