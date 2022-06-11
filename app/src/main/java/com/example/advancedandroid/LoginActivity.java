package com.example.advancedandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.Results;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ListView superListView;
   private String token = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

    }


    public void LoginAttempt(View view) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EditText User = findViewById(R.id.UserName);
        EditText PassWord = findViewById(R.id.Password);
        String user = User.getText().toString();
        String pass = PassWord.getText().toString();

        LoginAttempt(user, pass);

    }


    private void getUserList() {
        Call<List<Results>> call = RetrofitClient.getInstance().getMyApi().getUsers();
        call.enqueue(new Callback<List<Results>>() {
            @Override
            public void onResponse(Call<List<Results>> call, Response<List<Results>> response) {

                List<Results> Users = response.body();
                String[] oneHeroes = new String[Users.size()];

                for (int i = 0; i < Users.size(); i++) {
                    oneHeroes[i] = Users.get(i).getUserName();
                }

                superListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, oneHeroes));
            }

            @Override
            public void onFailure(Call<List<Results>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();

            }

        });
    }

    private void LoginAttempt(String user, String pass) {
       String arr[] = {user, pass};
        Call<String> call = RetrofitClient.getInstance().getMyApi().AttemptLogin(arr);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String Token = response.body();

                // failed login
                if(Token == null) {
                    // do something later
                } else {

                    token = Token;

                }


               // superListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, oneHeroes));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });



    }
}