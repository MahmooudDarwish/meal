package com.example.mealapp.feature.splash.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealapp.R;
import com.example.mealapp.feature.main.view.MainScreen;
import com.example.mealapp.feature.splash.presenter.ISplashPresenter;
import com.example.mealapp.feature.splash.presenter.SplashPresenter;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
import com.example.mealapp.utils.resource_helper.ResourceHelper;
import com.example.mealapp.utils.shared_preferences.SharedPreferencesManager;
import com.google.firebase.FirebaseApp;

import java.util.Locale;

public class Splash extends AppCompatActivity implements ISplash {
    ISplashPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        if (savedInstanceState != null) {
            String languageCode = presenter.getCurrentLang();
            setLocale(languageCode);
        }
        NetworkUtil.init(this);
        ResourceHelper.init(this);
        FirebaseApp.initializeApp(this);

         presenter = new SplashPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(this), SharedPreferencesManager.getInstance(this)));
        presenter.start();

    }

    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String languageCode = presenter.getCurrentLang();
        setLocale(languageCode);

    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(Splash.this, MainScreen.class);
        startActivity(intent);
        finish();
    }
}