package com.example.mealapp.feature.auth.sign_up.view;


public interface ISignUp {
    void showError(String message);

    void signUpSuccess();

    void hideLoading();

    void showLoading();
}