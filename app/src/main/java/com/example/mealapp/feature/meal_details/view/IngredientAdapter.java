package com.example.mealapp.feature.meal_details.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.utils.common_layer.models.Ingredient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        String ingredientTitle = ingredient.getQuantity() + ingredient.getName();
        holder.ingredientName.setText(ingredientTitle);
        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient.getName()+ "-Small" + ".png";
        Glide.with(holder.ingredientImage.getContext())
                .load(imageUrl)
                .into(holder.ingredientImage);

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ingredientImage;
        TextView ingredientName;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientName = itemView.findViewById(R.id.ingredientName);
        }
    }
}
