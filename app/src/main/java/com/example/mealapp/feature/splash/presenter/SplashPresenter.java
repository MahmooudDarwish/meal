package com.example.mealapp.feature.splash.presenter;

import android.os.Handler;

import com.example.mealapp.feature.splash.view.ISplash;

public class SplashPresenter implements ISplashPresenter{

    private final ISplash splashView;

    public SplashPresenter(ISplash view) {
        this.splashView = view;
    }

    @Override
    public void start() {
        new Handler().postDelayed(splashView::navigateToHome, 3000); // 3-second delay
    }
}
