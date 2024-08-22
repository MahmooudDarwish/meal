package com.example.mealapp.feature.meal_details.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealapp.R;
import com.example.mealapp.utils.common_layer.models.Ingredient;
import com.example.mealapp.utils.constants.ConstantKeys;

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
        String ingredientTitle = ingredient.getQuantity() +" "+ ingredient.getStrIngredient();
        holder.ingredientName.setText(ingredientTitle);
        String imageUrl = ConstantKeys.getIngredientImageUrl(ingredient.getStrIngredient());
                Glide.with(holder.ingredientImage.getContext())
                .load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ingredientImage);
        Animation slideInBottom = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_in_bottom);
        holder.itemView.startAnimation(slideInBottom);


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
