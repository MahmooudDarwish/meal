package com.example.mealapp.feature.auth.sign_up.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.example.mealapp.utils.common_layer.models.ValidationResult;
import com.example.mealapp.utils.validation_helpers.EmailValidator;
import com.example.mealapp.feature.auth.sign_up.view.ISignUp;
import com.example.mealapp.utils.validation_helpers.PasswordConfirmValidator;
import com.example.mealapp.utils.validation_helpers.PasswordValidator;
import com.example.mealapp.utils.firebase.FirebaseManager;

public class SignUpPresenter implements ISignUpPresenter {

    private final ISignUp view;

    private static final String TAG = "SignUpPresenter";

    public SignUpPresenter(ISignUp view) {
        this.view = view;
    }

    @Override
    public void validateAndSignUp(String email, String name, String password, String confirmPassword) {
        ValidationResult emailValidation = EmailValidator.isValidEmail(email);
        ValidationResult passwordValidation = PasswordValidator.validatePassword(password);
        ValidationResult confirmPasswordValidation = PasswordConfirmValidator.isPasswordMatching(password, confirmPassword);

        boolean isEmailValid = emailValidation.isValid();
        boolean isPasswordValid = passwordValidation.isValid();
        boolean isConfirmPasswordValid = confirmPasswordValidation.isValid();

        if (isEmailValid && !TextUtils.isEmpty(name) && isPasswordValid && isConfirmPasswordValid) {
            signUp(email, name, password);
        } else {
            view.showError("You should fix the above errors");
        }
    }

    @Override
    public ValidationResult validateEmail(String email) {
        return EmailValidator.isValidEmail(email);
    }

    @Override
    public ValidationResult validatePassword(String password) {
        return PasswordValidator.validatePassword(password);
    }

    @Override
    public ValidationResult validateConfirmPassword(String password, String confirmPassword) {
        return PasswordConfirmValidator.isPasswordMatching(password, confirmPassword);
    }

    private void signUp(String email ,String name, String password) {
        FirebaseManager.getInstance().signUp(email, password, name, task -> {
            if (task.isSuccessful()) {
                view.signUpSuccess();
            } else {
                Log.i(TAG, "createUserWithEmail:failure", task.getException());
                view.showError("Authentication failed. Please check your credentials and try again.");
            }
        });
    }

}
