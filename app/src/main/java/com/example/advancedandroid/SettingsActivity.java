package com.example.advancedandroid;

import static com.example.advancedandroid.api.Api.BASE_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.api.UnsafeOkHttpClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.installations.Utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity extends Activity {
    public Api api;
    public OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button dayButton = findViewById(R.id.ChangeThemeDay);
        Button nightButton = findViewById(R.id.ChangeThemeNight);

        dayButton.setOnClickListener(view -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        nightButton.setOnClickListener(view -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        });

        Button b = findViewById(R.id.back_to_app_button);
        b.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AppActivity.class)));
    }

    public void changeServer(View view) {
        api = RetrofitClient.getInstance().getMyApi();
        EditText server = findViewById(R.id.NewServer);
        String str = server.getText().toString();
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
        finish();
    }
}
