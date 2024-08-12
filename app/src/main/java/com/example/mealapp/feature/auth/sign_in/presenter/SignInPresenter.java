package com.example.mealapp.feature.auth.sign_in.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealapp.feature.auth.sign_in.view.ISignIn;
import com.example.mealapp.feature.auth.sign_in.view.OnUserRetrieveData;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.firebase.FirebaseManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SignInPresenter implements  ISignInPresenter{
    ISignIn view;
    private static final String TAG = "SignInPresenter";
    public SignInPresenter(ISignIn view) {
        this.view = view;
    }

    @Override
    public void signIn(String email, String password) {
        FirebaseManager.getInstance().signIn(email, password, task -> {
            if (task.isSuccessful()) {
                FirebaseManager.getInstance().getUserData(new OnUserRetrieveData() {
                    public void onUserDataRetrieved(User user) {
                        if (user != null) {
                            view.signInSuccess(user);
                        } else {
                            view.signInError("User data retrieval failed.");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        view.signInError("Error: " + e.getMessage());
                    }
                });
            } else {
                Log.i(TAG, "sign in:failure", task.getException());
                view.signInError("Authentication failed. Please check your credentials and try again.");
            }
        });
    }

    @Override
    public void signInWithGoogle(GoogleSignInAccount account) {
        FirebaseManager.getInstance().signInWithGoogle(account, task -> {
            if (task.isSuccessful()) {
                FirebaseManager.getInstance().getUserData(new OnUserRetrieveData() {
                    @Override
                    public void onUserDataRetrieved(User user) {
                        if (user != null) {
                            view.signInSuccess(user);
                        } else {
                            view.signInError("User data retrieval failed.");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        view.signInError("Error: " + e.getMessage());
                    }
                });
            } else {
                Log.i(TAG, "Google sign-in: failure", task.getException());
                view.signInError("Google sign-in failed. Please try again.");
            }
        });
    }

    @NonNull
    private static String getErrorMsg(Task<AuthResult> task) {
        String errorMsg;
        Exception exception = task.getException();
        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            errorMsg = "Invalid credentials. Please try again.";
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            errorMsg = "No account found with this email.";
        } else if (exception instanceof FirebaseAuthEmailException) {
            errorMsg = "Email verification is pending. Please verify your email.";
        } else {
            errorMsg = "Authentication failed. Please check your connection and try again.";
        }
        return errorMsg;
    }
}
