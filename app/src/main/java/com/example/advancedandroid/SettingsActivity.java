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
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button b = findViewById(R.id.back_to_app_button);
        b.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AppActivity.class)));

        /*
        @SuppressLint("WrongViewCast") Switch s = findViewById(R.id.ChangeTheme);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    s.setText("Light Mode");
                    reset();
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    s.setText("Dark Mode");
                    reset();
                }
            }
        });
        */

    }


    private void reset(){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void changeThemeDay(View view) {
        SetThemeDay(this);
        finish();
    }

    public void changeThemeNight(View view) {
        SetThemeNight(this);
        finish();
    }

    public void SetThemeDay(Activity activity)
    {
        activity.setTheme(R.style.Theme_AdvancedAndroidDay);
        this.reset();
    }

    public void SetThemeNight(Activity activity)
    {
        activity.setTheme(R.style.Theme_AdvancedAndroidNight);
        reset();
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
