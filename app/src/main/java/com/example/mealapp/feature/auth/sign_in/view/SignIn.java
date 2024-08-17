package com.example.mealapp.feature.auth.sign_in.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class SignIn extends BottomSheetDialogFragment implements ISignIn {

    private EditText emailField, passwordField;
    private Button signInBtn, signInWithGoogleBtn;
    private TextView signUpTextBtn;
    private ISignInPresenter presenter;
    private ProgressDialog progressDialog;
    private CheckBox staySignedIn;
    private static final int REQ_ONE_TAP = 2;
    private GoogleSignInClient googleSignInClient;
    private static final String TAG = "SignIn";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_sign_in, container, false); // Inflate your layout here
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        presenter = new SignInPresenter(this);
        initUI(view);
        setUpListeners();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setUpListeners() {
        signUpTextBtn.setOnClickListener(v -> {
            dismiss();
            SignUp signUpFragment = new SignUp();
            signUpFragment.show(requireActivity().getSupportFragmentManager(), "signUpFragment");
        });

        signInBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Email and Password must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            showLoading();
            presenter.signIn(email, password);
        });

        signInWithGoogleBtn.setOnClickListener(v -> signInWithGoogle());
    }

    private void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
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

    private void initUI(View view) {
        emailField = view.findViewById(R.id.emailField);
        passwordField = view.findViewById(R.id.passwordField);
        signInBtn = view.findViewById(R.id.signInBtn);
        signUpTextBtn = view.findViewById(R.id.signUpTextBtn);
        staySignedIn = view.findViewById(R.id.staySignedIn);
        signInWithGoogleBtn = view.findViewById(R.id.googleSignInBtn);
    }

    @Override
    public void signInSuccess(User user) {
        Log.i(TAG, "signInSuccess: " + user.getName());
        if (staySignedIn.isChecked()) {
            Log.i(TAG, "staySignedIn is checked:" + user.getName());
            addUserToSharedPreferences(user);
        }
        hideLoading();
        Toast.makeText(getActivity(), "Welcome!", Toast.LENGTH_LONG).show();
        dismiss();
        requireActivity().finish();
        Intent intent = new Intent(getActivity(), Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void addUserToSharedPreferences(User user) {
        Log.i(TAG, "addUserToSharedPreferences: " + user.getName());
        Log.i(TAG, "addUserToSharedPreferences: " + user.getEmail());
        SharedPreferences sharedPreferences = Objects.requireNonNull(requireActivity()).getSharedPreferences("user_data", Context.MODE_PRIVATE); // Use getActivity().MODE_PRIVATE
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", user.getName());
        editor.putString("user_email", user.getEmail());
        editor.apply();
    }

    @Override
    public void signInError(String errorMsg) {
        hideLoading();
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    showLoading();
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
