package com.example.mealapp.feature.search.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealapp.R;
import com.example.mealapp.feature.search.model.Country;
import android.view.LayoutInflater;

import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.AreaViewHolder> {
    private  List<Country> countryList;

    private final OnCountryClickedListener listener;
    private static final String TAG = "CountriesAdapter";
    public CountriesAdapter(List<Country> countryList, OnCountryClickedListener listener) {
        this.countryList = countryList;
        this.listener = listener;
    }

    public void setCountries(List<Country> filteredCountries) {
        this.countryList = filteredCountries;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: " + position + " " + countryList.get(position).getStrArea());
        String country = countryList.get(position).getStrArea();
        holder.countryName.setText(country);
        holder.itemView.setOnClickListener(v-> listener.onCountryClicked(country));

        Animation slideInBottom = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_in_right);

        holder.itemView.startAnimation(slideInBottom);

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }



    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        TextView countryName;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);
        }
    }
}

