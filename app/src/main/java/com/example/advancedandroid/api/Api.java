package com.example.advancedandroid.api;

import com.example.advancedandroid.models.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "https://10.0.2.2:7179/api/";

    @GET("Users")
    Call<List<Contact>> getUsers();

    // Token is supposed to be returned if successfully logged in.

    @POST("Users/Login")
    Call<String> AttemptLogin(@Body String[] arr);

    @GET("contacts")
    Call <List<Contact>> GetContacts(@Header("Authorization") String token);
}
