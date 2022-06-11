package com.example.advancedandroid.models;

import com.google.gson.annotations.SerializedName;



    public class Contact {

        // serialized name has to be the exact name is json form.
        // to see the name of the field you can use postman and see the returned json.

        @SerializedName("id")
        private String username;


        @SerializedName("name")
        private String nickname;

        @SerializedName("server")
         private String server;
        @SerializedName("last")
        private String last_message;

        @SerializedName("lastdate")
        private String last_Date;

        public Contact(String id, String name, String server, String last, String lastdate) {
            username = id;
            nickname = name;
            this.server = server;
            last_message = last;
            last_Date = lastdate;

        }

        public String getUserName() {
            return username;
        }

        public String getNickName() { return nickname;}

        public String getServer() { return server;}

        public String getLastMessage() { return last_message;}

        public String getLastDate() { return last_Date;}

    }



