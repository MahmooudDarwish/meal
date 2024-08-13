package com.example.mealapp.feature.home.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealapp.R;
import com.example.mealapp.feature.home.presenter.HomePresenter;
import com.example.mealapp.feature.home.presenter.IHomePresenter;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.feature.meals_viewer.view.MealsViewer;
import com.example.mealapp.utils.common_layer.models.Category;
import com.example.mealapp.utils.common_layer.models.Country;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;

import java.util.List;

public class HomeFragment extends Fragment implements  IHome, OnCategoryClickedListener, OnCountryClickedListener {

    ImageView mealOfDayImage;
    TextView mealOfDayTitle;
    CardView mealOfDayCard;
    RecyclerView categoriesRecycler;
    RecyclerView countriesRecycler;
    IHomePresenter presenter;
    private static final String TAG = "HomeFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new HomePresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance()));
        presenter.getRandomMeal();
        presenter.getAllCategories();
        presenter.getAllCountries();
        initUI(view);

        super.onViewCreated(view, savedInstanceState);
    }



    void initUI(View v){
        mealOfDayCard = v.findViewById(R.id.mealOfTheDayCard);
        mealOfDayImage = v.findViewById(R.id.mealImage);
        mealOfDayTitle = v.findViewById(R.id.mealName);
        categoriesRecycler = v.findViewById(R.id.categoriesRecycler);
        categoriesRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        countriesRecycler = v.findViewById(R.id.countriesRecycler);
        countriesRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    private void setUpListeners(String mealId) {
        mealOfDayCard.setOnClickListener(v -> {
            Log.i(TAG, "setUpListeners: mealOfDayCard Clicked");
            Intent intent = new Intent(getContext(), MealDetails.class);
            intent.putExtra("MEAL_ID", mealId);
            startActivity(intent);
        });
    }

    @Override
    public void showRandomMeal(PreviewMeal meal) {
        mealOfDayTitle.setText(meal.getStrMeal());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(mealOfDayImage);
        setUpListeners(meal.getIdMeal());



    }

    @Override
    public void showCategories(List<Category> categories) {
        CategoriesAdapter adapter = new CategoriesAdapter(categories, this);
        categoriesRecycler.setAdapter(adapter);
    }

    @Override
    public void showCountries(List<Country> countries) {
        CountriesAdapter adapter = new CountriesAdapter(countries, this);
        countriesRecycler.setAdapter(adapter);
    }

    @Override
    public void countryClicked(List<PreviewMeal> meals) {
        Intent intent = new Intent(getContext(), MealsViewer.class);
        startActivity(intent);

    }

    @Override
    public void categoryClicked(List<PreviewMeal> meals) {
        Intent intent = new Intent(getContext(), MealsViewer.class);
        startActivity(intent);
    }

    @Override
    public void onCategoryClicked(String categoryName) {
        presenter.getMealsByCategory(categoryName);
    }

    @Override
    public void onCountryClicked(String countryName) {
        presenter.getMealsByCountry(countryName);

    }
    @Override
    public void onFailureResult(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

}