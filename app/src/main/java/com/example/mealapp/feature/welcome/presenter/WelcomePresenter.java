package com.example.mealapp.feature.welcome.presenter;

import com.example.mealapp.feature.welcome.view.IWelcome;

public class WelcomePresenter implements  IWelcomePresenter {
    IWelcome view;

    public WelcomePresenter(IWelcome view){
        this.view = view;
    }

    @Override
    public void onSignInClicked() {
        view.navigateToSignIn();
    }

    @Override
    public void onSignUpClicked() {
        view.navigateToSignUp();
    }
}
