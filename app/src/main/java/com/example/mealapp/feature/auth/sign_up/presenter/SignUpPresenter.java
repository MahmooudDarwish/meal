package com.example.mealapp.feature.auth.sign_up.presenter;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_up.model.ValidationResult;
import com.example.mealapp.feature.auth.sign_up.model.validation_helpers.EmailValidator;
import com.example.mealapp.feature.auth.sign_up.view.ISignUp;
import com.example.mealapp.feature.auth.sign_up.model.validation_helpers.PasswordConfirmValidator;
import com.example.mealapp.feature.auth.sign_up.model.validation_helpers.PasswordValidator;
import com.example.mealapp.utils.firebase.FirebaseManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

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
            view.showError(view.getStringFromRes(R.string.fix_errors_message));
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

    private void signUp(String email, String name, String password) {
        view.showLoading();
        FirebaseManager.getInstance().signUp(email, password, name, task -> {
            if (task.isSuccessful()) {
                view.signUpSuccess();
            } else {
                String errorMsg = getSignUpErrorMsg(task);
                Log.i(TAG, "createUserWithEmail:failure", task.getException());
                view.showError(errorMsg);
            }
            view.hideLoading();
        });
    }

    @NonNull
    private String getSignUpErrorMsg(Task<AuthResult> task) {
        String errorMsg;
        Exception exception = task.getException();
        if (exception instanceof FirebaseAuthUserCollisionException) {
            errorMsg = view.getStringFromRes(R.string.account_exists_error);
        } else if (exception instanceof FirebaseAuthWeakPasswordException) {
            errorMsg = view.getStringFromRes(R.string.weak_password_error);
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            errorMsg = view.getStringFromRes(R.string.invalid_credentials_error);
        } else if (exception instanceof FirebaseNetworkException) {
            errorMsg = view.getStringFromRes(R.string.network_error);
        } else {
            errorMsg = view.getStringFromRes(R.string.sign_up_failed_error);
        }
        return errorMsg;
    }
}
