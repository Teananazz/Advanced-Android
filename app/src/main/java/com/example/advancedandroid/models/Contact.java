package com.example.advancedandroid.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


    @Entity(primaryKeys = {"username","usernameOfLooker"})

    public class Contact {

        // serialized name has to be the exact name is json form.
        // to see the name of the field you can use postman and see the returned json.

        @SerializedName("id")

        @NonNull
        private String username;


        @SerializedName("name")
        private String nickname;

        @SerializedName("server")
         private String server;
        @SerializedName("last")
        private String last_message ="";

        public String getUsernameOfLooker() {
            return usernameOfLooker;
        }

        public void setUsernameOfLooker(String usernameOfLooker) {
            this.usernameOfLooker = usernameOfLooker;
        }

        @NonNull
        @SerializedName("contactwith")
        private String usernameOfLooker;


        @SerializedName("lastdate")
        private String last_Date ="";
        public void setUsername(String username) {
            this.username = username;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public void setLast_message(String last_message) {
            this.last_message = last_message;
        }

        public void setLast_Date(String last_Date) {
            this.last_Date = last_Date;
        }



        public String getUsername() {
            return username;
        }

        public String getNickname() {
            return nickname;
        }

        public String getLast_message() {
            return last_message;
        }

        public String getLast_Date() {
            return last_Date;
        }





     //   public Contact(String id, String name, String server, String last, String lastdate, String usernameOfLooker) {
       //     username = id;
        //    nickname = name;
         //   this.server = server;
          //  last_message = last;
           // last_Date = lastdate;
           // this.usernameOfLooker = usernameOfLooker;
      //  }

        public Contact(String username, String nickname, String server, String last_message, String last_Date, String usernameOfLooker) {
            this.username = username;
            this.nickname = nickname;
            this.server = server;
            this.last_message = last_message;
            this.last_Date = last_Date;
            this.usernameOfLooker = usernameOfLooker;
        }

        public String getUserName() {
            return username;
        }

        public String getNickName() { return nickname;}

        public String getServer() { return server;}

        public String getLastMessage() { return last_message;}

        public String getLastDate() { return last_Date;}

        @Override
        public String toString() {
            return "Contact{" +
                    "username='" + username + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", server='" + server + '\'' +
                    '}';
        }
    }



