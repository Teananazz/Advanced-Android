package com.example.advancedandroid.api;

import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.models.Message;
import com.example.advancedandroid.models.SendStringAsObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.Header;

import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface Api {

    String BASE_URL = "https://10.0.2.2:7179/api/";

    @GET("Users")
    Call<List<Contact>> getUsers();

    // Token is supposed to be returned if successfully logged in.

    @POST("Users/Login")
    Call<ResponseBody> AttemptLogin(@Body String[] arr);

    @GET("contacts")
    Call <List<Contact>> GetContacts(@Header("Authorization") String token);

    @GET("contacts/{id}/messages")
    Call <List<Message>> getMessages(@Header("Authorization") String token, @Path("id") String id);


    @Headers({"Content-Type: application/json"})
    @POST("contacts/{id}/messages")
    Call <Void> PostMessages(@Header("Authorization") String token, @Path("id") String id, @Body String content);






}
