package com.example.mealapp.utils.validation_helpers;

import com.example.mealapp.utils.common_layer.models.ValidationResult;

public abstract class PasswordConfirmValidator {

    public static ValidationResult isPasswordMatching(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return new ValidationResult(true, null);
        } else {
            return new ValidationResult(false, "Passwords do not match");
        }
    }
}
