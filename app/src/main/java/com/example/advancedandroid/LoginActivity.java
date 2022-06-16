package com.example.advancedandroid;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.User;
import com.example.advancedandroid.room.AppDB;
import com.example.advancedandroid.room.UserDao;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ListView superListView;
   private String Token = null;
   List<User> Users;

    private UserDao UserDao;



    // we use this launcher in order to get permissions for api>=33 and others we might might need.
    private ActivityResultLauncher<String> requestPermissionLauncher ;

    // only in api 33
    private final int REQUEST_PERMISSION_POST_NOTIFICATIONS=1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setPermissionLauncher();
        RequestPermissions(); // without this nothing works properly - needs premission.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      // deleteDatabase("UserDB");
       // deleteDatabase("ContactsDB");
        //deleteDatabase("MessageDB");
        //finish();
        AppDB UserDatabase;
        UserDatabase = Room.databaseBuilder(getApplicationContext(), AppDB.class, "UserDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();

        UserDao = UserDatabase.UserDao();

        Users = UserDao.index();
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

    private void setPermissionLauncher() {
        requestPermissionLauncher=  registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {


                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Refused Permission")
                                    .setMessage("This app cannot proceed without the permission")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                        finish();
                    }
                });
                finish();
            }
        });

    }

    private void RequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            // to send notification in api>=33 we have to get permissions.
            RequestTiramisuPermission(Manifest.permission.POST_NOTIFICATIONS);

            // instead of permission READ_EXTERNAL_STORAGE -
            //
            //  in api>=33 we need to approach one of three permissions
            //Images and photos	READ_MEDIA_IMAGES
            //Videos	READ_MEDIA_VIDEO
            //Audio files	READ_MEDIA_AUDIO
            RequestTiramisuPermission(Manifest.permission.READ_MEDIA_IMAGES);

        }

        }
        private void RequestTiramisuPermission(final String permission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                int checkGranted = ContextCompat.checkSelfPermission(
                        this, permission);

                if (checkGranted == PackageManager.PERMISSION_GRANTED) {
                    // here we don't need to do anything as we have permission.

                }else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permission)) {
                    showExplanation("Permission Needed Else app won't work.", "Rationale", Manifest.permission.POST_NOTIFICATIONS, REQUEST_PERMISSION_POST_NOTIFICATIONS);
                } else {

                    requestPermissionLauncher.launch(
                            permission);
                }

            }
        }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermissionLauncher.launch(
                                permission);
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!isFinishing()){
                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("Refused Permission")
                                            .setMessage("This app cannot proceed without the permission")
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                }
                                finish();
                            }
                        });

                }});
        builder.create().show();
    }













    public void LoginAttempting(View view) {
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
                 Users = response.body();
                UserDao.insertAll(Users);

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
                    // successful login
                    if(response.code() == 200) {
                        Token = response.body().string();

                        Intent main_screen = new Intent(getApplicationContext(), AppActivity.class);
                        main_screen.putExtra("Token", Token);
                        main_screen.putExtra("User", user);
                        startActivity(main_screen);

                    }
                    else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!isFinishing()){
                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("Invalid Login Details")
                                            .setMessage("Your username or password are invalid. \n please try again.")
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                }
                            }
                        });

                    }


                }
                catch(Exception e) {

                }

                // failed login
                if(Token == null) {

                    // TODO: need to skip next few lines and also show a screen or something when happens.
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();

            }

        });

    }

    public void GoRegister(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        // TODO: need to check without returning like this.
        startActivity(i);
    }
}