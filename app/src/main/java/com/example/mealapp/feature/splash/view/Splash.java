package com.example.mealapp.feature.splash.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealapp.R;
import com.example.mealapp.feature.main.view.MainScreen;
import com.example.mealapp.feature.splash.presenter.ISplashPresenter;
import com.example.mealapp.feature.splash.presenter.SplashPresenter;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.resource_helper.ResourceHelper;
import com.google.firebase.FirebaseApp;

import java.util.Locale;

public class Splash extends AppCompatActivity implements  ISplash {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        NetworkUtil.init(this);
        ResourceHelper.init(this);
        FirebaseApp.initializeApp(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences(ConstantKeys.USER_SETTINGS, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(ConstantKeys.LANGUAGE_KEY)) {
            String savedLanguage = sharedPreferences.getString(ConstantKeys.LANGUAGE_KEY, ConstantKeys.KEY_EN);
            setLocale(savedLanguage);
        } else {
            String currentLanguage = Locale.getDefault().getLanguage();
            setLocale(currentLanguage);
        }



        ISplashPresenter presenter = new SplashPresenter(this);
        presenter.start();

    }
private void setLocale(String languageCode) {
    Locale locale = new Locale(languageCode);
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.setLocale(locale);
    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
}
    @Override
    public void navigateToHome() {
        Intent intent = new Intent(Splash.this, MainScreen.class);
        startActivity(intent);
        finish();
    }
}