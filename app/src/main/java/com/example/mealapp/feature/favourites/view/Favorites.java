package com.example.mealapp.feature.favourites.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.feature.favourites.presenter.FavoritesPresenter;
import com.example.mealapp.feature.favourites.presenter.IFavoritesPresenter;
import com.example.mealapp.feature.meal_details.view.MealDetails;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;

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
        super.onResume();
        presenter.getFavorites(this);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new FavoritesPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(requireActivity())));
        initUi(view);

        presenter.getFavorites(this);
        favouritesAdapter = new FavoriteMealsAdapter(new ArrayList<>(), this);

        favouritesRecyclerView.setAdapter(favouritesAdapter);


    }

    void initUi(View v){
        youDontHaveFavorites = v.findViewById(R.id.youDontHaveFavorites);
        youNeedToSignInFirst = v.findViewById(R.id.youNeedToSigninFirst);
        favouritesRecyclerView = v.findViewById(R.id.favoritesRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        favouritesRecyclerView.setLayoutManager(linearLayoutManager);

    }
    @Override
    public void showFavorites(List<FavoriteMeal> meals) {
        if (meals.isEmpty()){
            youDontHaveFavorites.setVisibility(View.VISIBLE);
            favouritesRecyclerView.setVisibility(View.GONE);
        }else{
            youDontHaveFavorites.setVisibility(View.GONE);
            favouritesRecyclerView.setVisibility(View.VISIBLE);
            favouritesAdapter.updateData(meals);

        }
    }

    @Override
    public void showLogin() {
        youNeedToSignInFirst.setVisibility(View.VISIBLE);
        favouritesRecyclerView.setVisibility(View.GONE);
        SignIn signInFragment = new SignIn();
        signInFragment.show(getParentFragmentManager(), "signInFragment");
    }

    @Override
    public void favoriteMealClicked(String mealID) {
        Intent intent = new Intent(requireActivity(), MealDetails.class);
        intent.putExtra("FAVORITE_MEAL_ID", mealID);
        startActivity(intent);
    }
}


/*
    @Override
    public void onClick(Product product) {
        favouritePresenter.deleteFromFav(product);
    }

 */