package com.example.mealapp.feature.auth.sign_in.view;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.mealapp.feature.main.view.MainScreen;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.connection_helper.NetworkUtil;
import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
import com.example.mealapp.utils.shared_preferences.SharedPreferencesManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class SignIn extends BottomSheetDialogFragment implements ISignIn {

    private EditText emailField, passwordField;
    private Button signInBtn, signInWithGoogleBtn;
    private TextView signUpTextBtn;
    private ISignInPresenter presenter;
    private ProgressDialog progressDialog;
    private CheckBox staySignedIn;
    private static final int RC_SIGN_IN = 9001;
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

        presenter = new SignInPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(requireActivity()),
                SharedPreferencesManager.getInstance(requireActivity())));

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
                Toast.makeText(getActivity(), getString(R.string.empty_email_password_message), Toast.LENGTH_SHORT).show();
                return;
            }
            showLoading();
            presenter.signIn(email, password);
        });

        signInWithGoogleBtn.setOnClickListener(v -> {
            if (NetworkUtil.isConnected()) {
                signInWithGoogle();
            } else {
                Toast.makeText(getActivity(), getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.signing_in_message));
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
        if (staySignedIn.isChecked()) {
            presenter.addUserToSharedPref();
        }
        hideLoading();
        Toast.makeText(getActivity(), getString(R.string.welcome_message), Toast.LENGTH_LONG).show();
        presenter.getDataFromFirebase();
        dismiss();
        requireActivity().finish();
        Intent intent = new Intent(getActivity(), MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void signInError(String errorMsg) {
        hideLoading();
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    showLoading();
                    presenter.signInWithGoogle(googleSignInAccount);
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign-in failed", e);
                signInError(getString(R.string.google_signin_failure_message));
            }
        }
    }


    @Override
    public String getStringFromRes(int resId){
        return getString(resId);
    }

    private void signInWithGoogle() {
        googleSignInClient.signOut();

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
}
