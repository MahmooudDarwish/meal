package com.example.mealapp.feature.welcome.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.feature.auth.sign_up.view.SignUp;
import com.example.mealapp.feature.home.view.Home;
import com.example.mealapp.feature.welcome.presenter.IWelcomePresenter;
import com.example.mealapp.feature.welcome.presenter.WelcomePresenter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Welcome extends AppCompatActivity implements  IWelcome{
    IWelcomePresenter presenter;
    Button signInButton;
    Button signUpButton;
    private static final String TAG = "Welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        presenter = new WelcomePresenter(this);
        initUI();
        setUpListeners();
        checkUserExist();

        }

    private void checkUserExist() {
        Log.i(TAG,"check user exist");
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);

        if (userEmail != null) {
            Log.i(TAG, "User email found: " + userEmail);
            navigateToHome();
        }
    }

    private void initUI(){
         signInButton = findViewById(R.id.signInButton);
         signUpButton = findViewById(R.id.signUpButton);

    }

    private void setUpListeners(){
        signInButton.setOnClickListener(v -> presenter.onSignInClicked());
        signUpButton.setOnClickListener(v -> presenter.onSignUpClicked());

    }

    @Override
    public void navigateToSignIn() {
        SignIn signInFragment = new SignIn();
        signInFragment.show(getSupportFragmentManager(), "signInFragment");
    }


    @Override
    public void navigateToSignUp() {
        Intent intent = new Intent(Welcome.this, SignUp.class);
        startActivity(intent);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(Welcome.this, Home.class);
        startActivity(intent);

    }
}