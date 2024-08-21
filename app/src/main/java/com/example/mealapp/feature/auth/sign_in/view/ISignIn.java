package com.example.mealapp.feature.auth.sign_in.view;

import com.example.mealapp.utils.common_layer.models.User;

public interface ISignIn {

     void signInSuccess(User user);
     void signInError(String errorMsg);
     String getStringFromRes(int resId);

}
