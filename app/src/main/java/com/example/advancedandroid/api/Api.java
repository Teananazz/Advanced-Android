package com.example.advancedandroid.api;

import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.models.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "https://10.0.2.2:7179/api/";

    @GET("Users")
    Call<List<Contact>> getUsers();

    // Token is supposed to be returned if successfully logged in.

    @POST("Users/Login")
    Call<String> AttemptLogin(@Body String[] arr);

    @GET("contacts")
    Call <List<Contact>> GetContacts(@Header("Authorization") String token);

    @GET("contacts/{id}/messages")
    Call <List<Message>> getMessages(@Header("Authorization") String token, @Path("id") String id);

    @POST("contacts/{id}/messages")

    Call <String> SendMessage(@Header("Authorization") String token, @Body String message, @Path("id") String id);


}
