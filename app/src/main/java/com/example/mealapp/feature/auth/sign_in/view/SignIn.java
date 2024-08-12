package com.example.mealapp.feature.auth.sign_in.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.presenter.ISignInPresenter;
import com.example.mealapp.feature.auth.sign_in.presenter.SignInPresenter;
import com.example.mealapp.feature.auth.sign_up.view.SignUp;
import com.example.mealapp.feature.home.view.Home;
import com.example.mealapp.utils.common_layer.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
public class SignIn extends AppCompatActivity implements  ISignIn{

    private EditText emailField, passwordField;
    private Button signInBtn, signInWithGoogleBtn, singInAsGuestBtn;
    private TextView signUpTextBtn;
    private ISignInPresenter presenter;
    private ProgressDialog progressDialog;
    private CheckBox staySignedIn;

    private static final int REQ_ONE_TAP = 2;
    private GoogleSignInClient googleSignInClient;
    private static final String TAG = "SignIn";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);


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

        signInWithGoogleBtn.setOnClickListener(v -> signInWithGoogle());
        singInAsGuestBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SignIn.this, Home.class);
            startActivity(intent);
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
        signInWithGoogleBtn = findViewById(R.id.googleSignInBtn);
        singInAsGuestBtn = findViewById(R.id.guestSignInBtn);

    }

    @Override
    public void signInSuccess(User user) {
        Log.i(TAG, "signInSuccess: " + user.getName());
        if(staySignedIn.isChecked()) {
            Log.i(TAG, "staySignedIn is checked:"  + user.getName());
            addUserToSharedPreferences(user);
        }
        hideLoading();
        Toast.makeText(this, "Welcome Back!", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    presenter.signInWithGoogle(googleSignInAccount);
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign-in failed", e);
                signInError("Google sign-in failed. Please try again.");
            }
        }
    }
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_ONE_TAP);
    }
}