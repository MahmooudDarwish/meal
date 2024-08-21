package com.example.mealapp.feature.auth.sign_up.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.feature.auth.sign_up.model.ValidationResult;
import com.example.mealapp.feature.auth.sign_up.presenter.ISignUpPresenter;
import com.example.mealapp.feature.auth.sign_up.presenter.SignUpPresenter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SignUp extends BottomSheetDialogFragment implements ISignUp {
    private EditText emailField, nameField, passwordField, confirmPasswordField;
    private Button signUpBtn;
    private TextView signInTextBtn;
    private ProgressDialog progressDialog;

    private ISignUpPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUI(view);
        presenter = new SignUpPresenter(this);

        setUpListeners();
    }

    private void initializeUI(View view) {
        emailField = view.findViewById(R.id.emailField);
        nameField = view.findViewById(R.id.nameField);
        passwordField = view.findViewById(R.id.passwordField);
        confirmPasswordField = view.findViewById(R.id.confirmPasswordField);
        signUpBtn = view.findViewById(R.id.signUpBtn);
        signInTextBtn = view.findViewById(R.id.signInTextBtn);
    }

    private void setUpListeners() {
        emailField.addTextChangedListener(createTextWatcher(presenter::validateEmail, emailField));
        passwordField.addTextChangedListener(createTextWatcher(presenter::validatePassword, passwordField));
        confirmPasswordField.addTextChangedListener(createTextWatcher(text -> {
            String password = passwordField.getText().toString();
            return presenter.validateConfirmPassword(password, text);
        }, confirmPasswordField));

        signInTextBtn.setOnClickListener(v -> {
            dismiss();
            SignIn signInFragment = new SignIn();
            signInFragment.show(requireActivity().getSupportFragmentManager(), "signInFragment");
        });

        signUpBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String name = nameField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            presenter.validateAndSignUp(email, name, password, confirmPassword);
        });
    }

    private TextWatcher createTextWatcher(Validator validator, EditText field) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ValidationResult result = validator.validate(s.toString());
                if (result.isValid()) {
                    field.setError(null);
                } else {
                    field.setError(result.getErrorMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.signing_up_message));
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public String getStringFromRes(int resId) {
        return getString(resId);
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @FunctionalInterface
    private interface Validator {
        ValidationResult validate(String text);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signUpSuccess() {
        hideLoading();
        Toast.makeText(getContext(), getString(R.string.sign_up_success_message), Toast.LENGTH_SHORT).show();
        dismiss();
        SignIn signInFragment = new SignIn();
        signInFragment.show(requireActivity().getSupportFragmentManager(), "signInFragment");
    }
}
