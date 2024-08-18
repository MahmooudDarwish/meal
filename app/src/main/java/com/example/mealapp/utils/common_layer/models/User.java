package com.example.mealapp.utils.common_layer.models;
public class User {
    final private String email;
    final private String name;

    private String uid;



    public User(String email, String name, String uid) {
        this.email = email;
        this.name = name;
        this.uid = uid;
    }

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

    public String getUid(){
        return uid;
    }
    public void setUid(String uid){
        this.uid = uid;
    }
}
