package com.example.mealapp.feature.auth.sign_in.view;

import com.example.mealapp.utils.common_layer.models.User;

public interface ISignIn {

    public void signInSuccess(User user);
    public void signInError(String errorMsg);

}
