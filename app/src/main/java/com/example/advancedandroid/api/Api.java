package com.example.advancedandroid.api;

import com.example.advancedandroid.models.Results;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    String BASE_URL = "https://10.0.2.2:7179/api/";

    @GET("Users")
    Call<List<Results>> getUsers();

    // Token is supposed to be returned if successfully logged in.

    @POST("Users/Login")
    Call<String> AttemptLogin(@Body String[] arr);
}
