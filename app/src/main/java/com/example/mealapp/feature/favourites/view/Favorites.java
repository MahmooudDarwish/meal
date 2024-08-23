package com.example.mealapp.feature.favourites.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mealapp.R;
import com.example.mealapp.feature.favourites.presenter.FavoritesPresenter;
import com.example.mealapp.feature.favourites.presenter.IFavoritesPresenter;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
import com.example.mealapp.utils.shared_preferences.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;


public class Favorites extends Fragment implements IFavorites, OnFavoriteMealClick{

    IFavoritesPresenter presenter;
    FavoriteMealsAdapter favouritesAdapter;
    TextView youDontHaveFavorites;
    TextView youNeedToSignInFirst;

    RecyclerView favouritesRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onResume() {
        Log.d("TAG", "onResume:herreee ");
        super.onResume();
        presenter.getFavorites(this);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new FavoritesPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(requireActivity()), SharedPreferencesManager.getInstance(requireActivity())));
        initUi(view);

        favouritesAdapter = new FavoriteMealsAdapter(new ArrayList<>(), this);
        favouritesRecyclerView.setAdapter(favouritesAdapter);


    }

    void initUi(View v){
        youDontHaveFavorites = v.findViewById(R.id.youDontHaveFavorites);
        youNeedToSignInFirst = v.findViewById(R.id.youNeedToSigninFirst);
        favouritesRecyclerView = v.findViewById(R.id.favoritesRecycler);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        }else{
            favouritesRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2,LinearLayoutManager.VERTICAL, false));

        }

    }
    @Override
    public void showFavorites(List<FavoriteMeal> meals) {
        if (meals.isEmpty()){
            Log.d("TAG", "showFavorites: empty");
            youDontHaveFavorites.setVisibility(View.VISIBLE);
            favouritesRecyclerView.setVisibility(View.GONE);
        }else{
            Log.d("TAG", "showFavorites: not empty");
            youDontHaveFavorites.setVisibility(View.GONE);
            favouritesRecyclerView.setVisibility(View.VISIBLE);
            favouritesAdapter.updateData(meals);
        }
    }

    @Override
    public void showGuestMsg() {
        youNeedToSignInFirst.setVisibility(View.VISIBLE);
        favouritesRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void favoriteMealClicked(String mealID) {
        Intent intent = new Intent(requireActivity(), MealDetails.class);
        intent.putExtra(ConstantKeys.FAVORITE_MEAL_ID, mealID);
        startActivity(intent);
    }
}


