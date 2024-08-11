package com.example.mealapp.feature.auth.sign_in.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.presenter.ISignInPresenter;
import com.example.mealapp.feature.auth.sign_in.presenter.SignInPresenter;
import com.example.mealapp.feature.auth.sign_up.view.SignUp;
import com.example.mealapp.feature.home.view.Home;
import com.example.mealapp.utils.common_layer.models.User;

public class SignIn extends AppCompatActivity implements  ISignIn{

    private EditText emailField, passwordField;
    private Button signInBtn;
    private TextView signUpTextBtn;
    private ISignInPresenter presenter;
    private ProgressDialog progressDialog;
    private CheckBox staySignedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        presenter = new SignInPresenter(this);
        initUI();
        setUpListeners();
    }

    private void setUpListeners() {
        signUpTextBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SignIn.this, SignUp.class);
            startActivity(intent);
        });

        signInBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and Password must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            showLoading();
            presenter.signIn(email, password);
        });


    }

    private void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing in...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void initUI() {
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        signInBtn = findViewById(R.id.signInBtn);
        signUpTextBtn = findViewById(R.id.signUpTextBtn);
        staySignedIn = findViewById(R.id.staySignedIn);
    }

    @Override
    public void signInSuccess(User user) {
        if(staySignedIn.isChecked()) {
            addUserToSharedPreferences(user);
        }
        hideLoading();
        Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignIn.this, Home.class);
        startActivity(intent);
    }

    private void addUserToSharedPreferences(User user){
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", user.getName());
        editor.putString("user_email", user.getEmail());
        editor.apply();

    }
    @Override
    public void signInError(String errorMsg) {
        hideLoading();
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}