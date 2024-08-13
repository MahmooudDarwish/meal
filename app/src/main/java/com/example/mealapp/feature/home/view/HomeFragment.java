package com.example.mealapp.feature.home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.mealapp.utils.common_layer.models.Category;
import com.example.mealapp.utils.common_layer.models.Country;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;

import java.util.List;

public class HomeFragment extends Fragment implements  IHome {

    ImageView mealOfDayImage;
    TextView mealOfDayTitle;
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
        mealOfDayImage = v.findViewById(R.id.mealOfTheDayImage);
        mealOfDayTitle = v.findViewById(R.id.mealOfTheDayTitle);
        categoriesRecycler = v.findViewById(R.id.categoriesRecycler);
        categoriesRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        countriesRecycler = v.findViewById(R.id.countriesRecycler);
        countriesRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void showRandomMeal(PreviewMeal meal) {
        mealOfDayTitle.setText(meal.getStrMeal());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(mealOfDayImage);
    }

    @Override
    public void showCategories(List<Category> categories) {
        CategoriesAdapter adapter = new CategoriesAdapter(categories);
        categoriesRecycler.setAdapter(adapter);
    }

    @Override
    public void showCountries(List<Country> countries) {
        CountriesAdapter adapter = new CountriesAdapter(countries);
        countriesRecycler.setAdapter(adapter);
    }


    @Override
    public void onFailureResult(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}