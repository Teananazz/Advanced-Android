package com.example.advancedandroid;

import static com.example.advancedandroid.api.Api.BASE_URL;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.api.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity extends Activity {
    public Api api;
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        api = RetrofitClient.getInstance().getMyApi();
        String str = "we will insert here the text from the screen";
        String httpStr = "https://";
        String apiStr = "/api/";
        if (!str.contains(httpStr)) {
            str = httpStr + str;
        }
        if (!str.contains(apiStr)) {
            str = str + httpStr;
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(str)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }
}
