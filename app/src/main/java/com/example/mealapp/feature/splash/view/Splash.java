package com.example.mealapp.feature.splash.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealapp.R;
import com.example.mealapp.feature.main.view.MainScreen;
import com.example.mealapp.feature.splash.presenter.ISplashPresenter;
import com.example.mealapp.feature.splash.presenter.SplashPresenter;
import com.google.firebase.FirebaseApp;

public class Splash extends AppCompatActivity implements  ISplash {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);



        ISplashPresenter presenter = new SplashPresenter(this);
        presenter.start();

    }
    @Override
    public void navigateToHome() {
        Intent intent = new Intent(Splash.this, MainScreen.class);
        startActivity(intent);
        finish();
    }
}