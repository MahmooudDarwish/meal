package com.example.mealapp.feature.favourites.presenter;


import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.mealapp.feature.favourites.view.IFavorites;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.data_source_manager.MealRepository;


public class FavoritesPresenter implements  IFavoritesPresenter {

    private final IFavorites _view;

    private final MealRepository _repo;

    public FavoritesPresenter(IFavorites view, MealRepository repo) {
        _view = view;
        _repo = repo;

    }

    @Override
    public void getFavorites(LifecycleOwner owner) {
        boolean isGuest = UserSessionHolder.isGuest();
        if (isGuest) {
            _view.showGuestMsg();
        } else {
            String userId = UserSessionHolder.getInstance().getUser().getUid();
            String email = UserSessionHolder.getInstance().getUser().getEmail();
            String displayName = UserSessionHolder.getInstance().getUser().getName();
            Log.d("userId", "getFavorites: " + email);
            Log.d("userId", "getFavorites: " + displayName);
            Log.d("userId", "getFavorites: " + userId);
            _repo.getAllFavoriteMealsForUser(userId).observe(owner, _view::showFavorites);
        }
    }

}
