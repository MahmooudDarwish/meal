package com.example.mealapp.feature.auth.sign_in.presenter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface ISignInPresenter {

     void signIn(String email, String password);
     void signInWithGoogle(GoogleSignInAccount account);

     void addUserToSharedPref();
}
