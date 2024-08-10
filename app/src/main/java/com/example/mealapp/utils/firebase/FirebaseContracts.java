package com.example.mealapp.utils.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

public interface FirebaseContracts {

    public interface FirebaseSignIn {
        void signUp(@NonNull String email, @NonNull String password, @NonNull OnCompleteListener<AuthResult> onCompleteListener);
    }
    public interface FirebaseSignUp {
        void signUp(@NonNull String email, @NonNull String password, @NonNull OnCompleteListener<AuthResult> onCompleteListener);
    }
    public interface FirebaseGoogleSignIn {
    }
    public interface FirebaseLogout {
    }
    public interface FirebaseGetUser {
    }
}
