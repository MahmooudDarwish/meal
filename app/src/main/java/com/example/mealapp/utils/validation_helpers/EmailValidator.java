package com.example.mealapp.utils.validation_helpers;

import android.text.TextUtils;
import android.util.Patterns;

import com.example.mealapp.utils.common_layer.models.ValidationResult;

public abstract class EmailValidator {

    public static ValidationResult isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return new ValidationResult(false, "Email cannot be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return new ValidationResult(false, "Invalid email address");
        } else {
            return new ValidationResult(true, null);
        }
    }
}
