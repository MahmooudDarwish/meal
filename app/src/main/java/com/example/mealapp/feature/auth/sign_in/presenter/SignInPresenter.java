package com.example.mealapp.feature.auth.sign_in.presenter;

import android.util.Log;


import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.ISignIn;
import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.data_source_manager.MealRepository;
import com.example.mealapp.utils.firebase.OnUserRetrieveData;
import com.example.mealapp.utils.common_layer.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SignInPresenter implements ISignInPresenter {
    ISignIn view;
    
    MealRepository repo;
    private static final String TAG = "SignInPresenter";

    public SignInPresenter(ISignIn view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }


    @Override
    public void addUserToSharedPref(){
        String name = UserSessionHolder.getInstance().getUser().getName();
        String email = UserSessionHolder.getInstance().getUser().getEmail();
        repo.savePreference(ConstantKeys.USER_NAME, name, true);
        repo.savePreference(ConstantKeys.USER_EMAIL, email, true);
        repo.savePreference(ConstantKeys.STAY_LOGGED_IN, true, true);

    }
    @Override
    public void signIn(String email, String password) {
        repo.signIn(email, password, task -> {
            if (task.isSuccessful()) {
                repo.getCurrentUser(new OnUserRetrieveData() {
                    @Override
                    public void onUserDataRetrieved(User user) {
                        if (user != null) {
                            repo.saveUserData(user);
                            UserSessionHolder.getInstance().setUser(user);
                            view.signInSuccess(user);
                        } else {
                            view.signInError(view.getStringFromRes(R.string.user_data_retrieval_failed));
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        view.signInError(view.getStringFromRes(R.string.sign_in_failed) + " " + e.getMessage());
                    }
                });
            } else {
                String errorMsg = getErrorMsg(task);
                Log.i(TAG, "sign in: failure", task.getException());
                view.signInError(errorMsg);
            }
        });
    }

    @Override
    public void signInWithGoogle(GoogleSignInAccount account) {
        repo.signInWithGoogle(account, task -> {
            if (task.isSuccessful()) {
                repo.getCurrentUser(new OnUserRetrieveData() {
                    @Override
                    public void onUserDataRetrieved(User user) {
                        if (user != null) {
                            UserSessionHolder.getInstance().setUser(user);
                            view.signInSuccess(user);
                        } else {
                            view.signInError(view.getStringFromRes(R.string.user_data_retrieval_failed));
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        view.signInError(view.getStringFromRes(R.string.sign_in_failed) + " " + e.getMessage());
                    }
                });
            } else {
                Log.i(TAG, "Google sign-in: failure", task.getException());
                view.signInError(view.getStringFromRes(R.string.google_sign_in_failed));
            }
        });
    }

    private String getErrorMsg(Task<AuthResult> task) {
        Exception exception = task.getException();
        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return view.getStringFromRes(R.string.invalid_credentials);
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return view.getStringFromRes(R.string.invalid_user);
        } else if (exception instanceof FirebaseNetworkException) {
            return view.getStringFromRes(R.string.network_error);
        } else {
            return view.getStringFromRes(R.string.sign_in_failed);
        }
    }
}
