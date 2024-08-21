package com.example.mealapp.feature.favourites.view;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;

import java.util.List;

public interface IFavorites {
     void showFavorites(List<FavoriteMeal> products);

    void showGuestMsg();
}
