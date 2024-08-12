package com.example.mealapp.feature.auth.sign_in.presenter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface ISignInPresenter {

    public void signIn(String email, String password);
    public void signInWithGoogle(GoogleSignInAccount account);
}
