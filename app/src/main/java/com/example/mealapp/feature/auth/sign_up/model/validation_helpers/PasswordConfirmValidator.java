package com.example.mealapp.feature.auth.sign_up.model.validation_helpers;

import com.example.mealapp.feature.auth.sign_up.model.ValidationResult;

public abstract class PasswordConfirmValidator {

    public static ValidationResult isPasswordMatching(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return new ValidationResult(true, null);
        } else {
            return new ValidationResult(false, "Passwords do not match");
        }
    }
}
