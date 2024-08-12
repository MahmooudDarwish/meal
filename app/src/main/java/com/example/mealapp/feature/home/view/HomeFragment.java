package com.example.mealapp.feature.home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealapp.R;
import com.example.mealapp.feature.home.presenter.HomePresenter;
import com.example.mealapp.feature.home.presenter.IHomePresenter;
import com.example.mealapp.utils.common_layer.models.PreviewMeal;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;

public class HomeFragment extends Fragment implements  IHome {


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
       //, ProductLocalDataSourceImpl.getInstance(this)
        presenter = new HomePresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance()));
        presenter.getRandomMeal();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showRandomMeal(PreviewMeal meal) {
        Log.i(TAG, "showRandomMeal: " + meal.getIdMeal());

    }

    @Override
    public void onFailureResult(String errorMsg) {

    }
}