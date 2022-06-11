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
import com.example.advancedandroid.models.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ListView superListView;
   private String Token = null;
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

        // if token is not null then login is successful.

    }


    private void getUserList() {
        Call<List<Contact>> call = RetrofitClient.getInstance().getMyApi().getUsers();
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {

                List<Contact> Users = response.body();
                String[] oneHeroes = new String[Users.size()];

                for (int i = 0; i < Users.size(); i++) {
                    oneHeroes[i] = Users.get(i).getUserName();
                }

                superListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, oneHeroes));
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
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

                 Token = response.body();

                // failed login
                if(Token == null) {

                    // TODO: need to skip next few lines and also show a screen or something when happens.
                }

                Intent main_screen = new Intent(getApplicationContext(), AppActivity.class);
                main_screen.putExtra("Token", Token);
                main_screen.putExtra("User", user);
                startActivity(main_screen);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });

    }
}