package com.example.advancedandroid.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userName")
    private String userName;


    public User(String userName) {
       this.userName = userName;

    }

   public String getUserName() {
        return userName;
    }



}
