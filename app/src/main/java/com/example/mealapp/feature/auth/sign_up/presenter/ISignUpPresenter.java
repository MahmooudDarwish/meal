package com.example.mealapp.feature.auth.sign_up.presenter;

import com.example.mealapp.feature.auth.sign_up.model.ValidationResult;

public interface ISignUpPresenter {

    void validateAndSignUp(String email, String name, String password, String confirmPassword);
    ValidationResult validateEmail(String email);
    ValidationResult validatePassword(String password);

    ValidationResult validateConfirmPassword(String password, String confirmPassword);
}
