package com.example.advancedandroid.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class User {


    @SerializedName("userName")
    @PrimaryKey
    @NonNull
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
