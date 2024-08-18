package com.example.mealapp.utils.common_layer.models;

import java.util.Objects;

public class UserSessionHolder {

    private static User user = null;

    private static final UserSessionHolder instance = new UserSessionHolder();

    public static UserSessionHolder getInstance() {
        return instance;
    }
    private UserSessionHolder() {
    }

    public void setUser(User user) {
        UserSessionHolder.user = user;
    }

    public User getUser() {
        return user;
    }

    public static boolean isGuest() {
        return user == null ||
                (Objects.equals(user.getName(), "") && Objects.equals(user.getEmail(), ""));
    }

}
