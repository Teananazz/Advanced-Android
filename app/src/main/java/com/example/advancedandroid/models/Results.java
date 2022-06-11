package com.example.advancedandroid.models;

import com.google.gson.annotations.SerializedName;



    public class Results {

        // serialized name has to be the exact name is json form.
        // to see the name of the field you can use postman and see the returned json.
        @SerializedName("userName")
        private String username;


        public Results(String userName) {
            this.username = userName;
        }

        public String getUserName() {
            return username;
        }
    }



