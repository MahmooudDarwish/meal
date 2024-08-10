package com.example.mealapp.utils.validation_helpers;

import com.example.mealapp.utils.common_layer.models.ValidationResult;

public abstract class PasswordValidator {

    public static ValidationResult validatePassword(String password) {
        if (password.length() < 8) {
            return new ValidationResult(false, "Password must be at least 8 characters long");
        } else if (!hasUpperCase(password)) {
            return new ValidationResult(false, "Password must contain at least one uppercase letter");
        } else if (!hasLowerCase(password)) {
            return new ValidationResult(false, "Password must contain at least one lowercase letter");
        } else if (!hasDigit(password)) {
            return new ValidationResult(false, "Password must contain at least one digit");
        } else if (!hasSpecialChar(password)) {
            return new ValidationResult(false, "Password must contain at least one special character");
        } else {
            return new ValidationResult(true, null);
        }
    }

    private static boolean hasUpperCase(String str) {
        return !str.equals(str.toLowerCase());
    }

    private static boolean hasLowerCase(String str) {
        return !str.equals(str.toUpperCase());
    }

    private static boolean hasDigit(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSpecialChar(String str) {
        return str.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
}
