package com.example.advancedandroid.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class User {
    @SerializedName("userName")
    @PrimaryKey
    private String userName;

    @SerializedName("img")
    private String img;

    public User(String userName, String img) {
       this.userName = userName;
       this.img = img;

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUserName() {
        return userName;
    }

    public String getImg() { return img;}


}
