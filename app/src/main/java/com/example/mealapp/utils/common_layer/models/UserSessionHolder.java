package com.example.mealapp.utils.common_layer.models;


import android.util.Log;

public class UserSessionHolder {

    private static User user = null;

    private static UserSessionHolder instance;

    public static UserSessionHolder getInstance() {
        Log.d("UserSessionHolder", "getInstance: ");
        if (instance == null) {
            instance = new UserSessionHolder();
        }
        return instance;
    }
    private UserSessionHolder() {
    }

    public void setUser(User user) {
        Log.d("UserSessionHolder", "setUser: " + user);
        UserSessionHolder.user = user;
    }


    public User getUser() {
        return user;
    }

    public static boolean isGuest() {
        return user == null;
    }

}
