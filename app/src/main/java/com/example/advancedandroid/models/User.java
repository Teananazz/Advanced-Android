package com.example.advancedandroid.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userName")
    private String userName;

    @SerializedName("img")
    private String img;

    public User(String userName, String img) {
       this.userName = userName;
       this.img = img;

    }



   public String getUserName() {
        return userName;
    }

    public String getImg() { return img;}


}
