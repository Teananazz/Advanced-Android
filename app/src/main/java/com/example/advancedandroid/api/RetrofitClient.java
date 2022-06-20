package com.example.advancedandroid.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private static Api myApi;
    private static Api Custom_Api;
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public  void AddClient(String url) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Custom_Api = retrofit.create(Api.class);
    }

    public Api getMyApi() {
        return myApi;
    }
    public Api getCustom_Api() {
        return Custom_Api;
    }

    public void changeMain(String url) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi =  retrofit.create(Api.class); // instead of deep copying we just change variables.

    }
}
