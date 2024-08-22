package com.example.mealapp.feature.search.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.feature.search.model.Category;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private  List<Category> categoryList;

    private final OnCategoryClickedListener listener;

    public CategoriesAdapter(List<Category> categoryList, OnCategoryClickedListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    public void setCategories(List<Category> filteredCategories) {
        this.categoryList = filteredCategories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getStrCategory());
        Glide.with(holder.itemView.getContext())
                .load(category.getStrCategoryThumb())
                .into(holder.categoryImage);

        holder.itemView.setOnClickListener(v -> listener.onCategoryClicked(category.getStrCategory()));
        Animation slideInBottom = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_in_top);

      //  holder.itemView.startAnimation(fadeIn);
        holder.itemView.startAnimation(slideInBottom);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }



    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        CircleImageView categoryImage;
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}
