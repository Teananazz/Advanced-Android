package com.example.advancedandroid;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.advancedandroid.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ListView superListView;
   private String Token = null;
   List<User> Users;

//    ActivityResultLauncher<Intent> Launcher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//
//                    // 1 here means register was successful.
//                    //TODO: what happens after here?
//                    if (result.getResultCode() == 1) {
//
//
//                    }
//                    // register failed
//                    //TODO: write something?
//                    if(result.getResultCode() == 2) {
//
//
//                    }
//                }
//            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {

            Intent i = new Intent(this, RegisterActivity.class);
            // TODO: need to check without returning like this.
            startActivity(i);
        });
         // we update the User list.
        CheckUserList("", 1);
        // this intent is for checking if user exists
        // TODO: this is very bad way to check which intent we are here for.
        // TODO: better to send a flag which will indicate where we came from.
        Intent intent = getIntent();
        if(intent != null) {
            String userCheck = intent.getStringExtra("UserNameCheck");
            if(userCheck!= null) {
                CheckUserList(userCheck, 2);
            }
        }

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


    private void CheckUserList(String userCheck, int flag) {
        Call<List<User>> call = RetrofitClient.getInstance().getMyApi().getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> Users = response.body();
             if( flag == 2) {

                 String[] UserNames = new String[Users.size()];
                 int i;
                 for ( i = 0; i < Users.size(); i++) {
                     UserNames[i] = Users.get(i).getUserName();
                     if(UserNames[i].equals( userCheck)) {
                         break;
                     }
                 }
                 Intent r = new Intent();

                 if( i == Users.size()) {

                     setResult(1, r);
                     finish();
                 }
                 else {
                     setResult(2,r);
                     finish();
                 }
             }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();

            }

        });
    }

    private void LoginAttempt(String user, String pass) {
       String arr[] = {user, pass};
        Call<ResponseBody> call = RetrofitClient.getInstance().getMyApi().AttemptLogin(arr);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Token = response.body().string();
                }
                catch(Exception e) {

                }

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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });

    }
}