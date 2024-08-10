package com.example.mealapp.feature.splash.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealapp.R;
import com.example.mealapp.feature.splash.presenter.ISplashPresenter;
import com.example.mealapp.feature.splash.presenter.SplashPresenter;
import com.example.mealapp.feature.welcome.view.Welcome;
import com.google.firebase.FirebaseApp;

public class Splash extends AppCompatActivity implements  ISplash {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);



        ImageView logoView = findViewById(R.id.logoView);
        setAnimation(logoView);
        ISplashPresenter presenter = new SplashPresenter(this);
        presenter.start();

    }


    public void setAnimation(ImageView view){
        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        view.startAnimation(bounceAnimation);
    }
    @Override
    public void navigateToWelcome() {
        Intent intent = new Intent(Splash.this, Welcome.class);
        startActivity(intent);
        finish();
    }
}