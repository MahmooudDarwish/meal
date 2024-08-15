package com.example.mealapp.utils.firebase;

import com.example.mealapp.utils.common_layer.models.User;

public interface OnUserRetrieveData {

    void onUserDataRetrieved(User user);
    void onError(Exception e);

}
