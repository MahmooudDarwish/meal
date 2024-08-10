package com.example.mealapp.feature.welcome.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.feature.auth.sign_up.view.SignUp;
import com.example.mealapp.feature.welcome.presenter.IWelcomePresenter;
import com.example.mealapp.feature.welcome.presenter.WelcomePresenter;

public class Welcome extends AppCompatActivity implements  IWelcome{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        IWelcomePresenter presenter = new WelcomePresenter(this);

        Button signInButton = findViewById(R.id.signInButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(v -> presenter.onSignInClicked());
        signUpButton.setOnClickListener(v -> presenter.onSignUpClicked());
    }

    @Override
    public void navigateToSignIn() {
        Intent intent = new Intent(Welcome.this, SignIn.class);
        startActivity(intent);
    }

    @Override
    public void navigateToSignUp() {
        Intent intent = new Intent(Welcome.this, SignUp.class);
        startActivity(intent);
    }
}