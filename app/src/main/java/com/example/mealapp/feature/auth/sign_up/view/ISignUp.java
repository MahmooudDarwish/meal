package com.example.mealapp.feature.auth.sign_up.view;


import com.google.firebase.auth.FirebaseUser;

public interface ISignUp {
    void showError(String message);
    void signUpSuccess(FirebaseUser user);
}