package com.example.mealapp.utils.common_layer.models;
public class User {
    final private String email;
    final private String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }

}
