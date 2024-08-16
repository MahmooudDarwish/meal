package com.example.mealapp.feature.search.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.utils.common_layer.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> implements Filterable {

    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<Ingredient> filteredIngredients = new ArrayList<>();
    private final IngredientFilter ingredientFilter = new IngredientFilter();
    private final LoadMoreListener loadMoreListener;
    private boolean isLoading = false;

    public IngredientAdapter(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
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

        if (position == getItemCount() - 1 && !isLoading) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
        return filteredIngredients.size();
    }

    @Override
    public Filter getFilter() {
        return ingredientFilter;
    }

    private class IngredientFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Ingredient> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(ingredients);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Ingredient item : ingredients) {
                    if (item.getStrIngredient().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            Log.d("IngredientFilter", "Filtered count: " + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredIngredients.clear();
            filteredIngredients.addAll((List<Ingredient>) results.values);
            notifyDataSetChanged();
        }
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
