package com.example.mealapp.feature.auth.sign_up.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mealapp.R;
import com.example.mealapp.feature.auth.sign_in.view.SignIn;
import com.example.mealapp.utils.common_layer.models.ValidationResult;
import com.example.mealapp.feature.auth.sign_up.presenter.ISignUpPresenter;
import com.example.mealapp.feature.auth.sign_up.presenter.SignUpPresenter;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements ISignUp {
    private EditText emailField, nameField, passwordField, confirmPasswordField;
    private Button signUpBtn;
    private TextView signInTextBtn;
    private ISignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        initializeUI();
        presenter = new SignUpPresenter(this);

        setUpListeners();
    }

    private void initializeUI() {
        emailField = findViewById(R.id.emailField);
        nameField = findViewById(R.id.nameField);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);
        signUpBtn = findViewById(R.id.signUpBtn);
        signInTextBtn = findViewById(R.id.signInTextBtn);
    }

    private void setUpListeners() {
        emailField.addTextChangedListener(createTextWatcher(presenter::validateEmail, emailField));
        passwordField.addTextChangedListener(createTextWatcher(presenter::validatePassword, passwordField));
        confirmPasswordField.addTextChangedListener(createTextWatcher(text -> {
            String password = passwordField.getText().toString();
            return presenter.validateConfirmPassword(password, text);
        }, confirmPasswordField));

        signInTextBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
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

    @FunctionalInterface
    private interface Validator {
        ValidationResult validate(String text);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signUpSuccess(FirebaseUser user) {
        Toast.makeText(this, "Sign Up Successful! Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUp.this, SignIn.class);
        startActivity(intent);
    }
}