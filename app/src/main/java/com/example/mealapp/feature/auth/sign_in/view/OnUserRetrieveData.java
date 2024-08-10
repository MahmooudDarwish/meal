package com.example.mealapp.feature.auth.sign_in.view;

import com.example.mealapp.utils.common_layer.models.User;

public interface OnUserRetrieveData {

    void onUserDataRetrieved(User user);
    void onError(Exception e);

}
