package com.example.advancedandroid;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
   ImageView PhotoProfilePreview = null;
   TextView PromtProfile = null;
     String img = null;
     String user;
     String pass;
     String display;
     Api api = null;
    ActivityResultLauncher<String> Launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    //TODO: save String representation in data base when creating user.
                    // and Retrieve it later.
                      PhotoProfilePreview.setImageURI(result);
                    img = result.toString();

                }
            });

    ActivityResultLauncher<Intent> Launcher_UserCheck = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    // 1 if  no such User exists.
                    if (result.getResultCode() == 1) {

                       CreateUser(user,pass, display,img);

                    }
                    // 2 if that user already exists
                    if(result.getResultCode() == 2) {
                        // there is probably a better way to show error.
                        Toast.makeText(getApplicationContext(), "This user name is already taken", Toast.LENGTH_LONG).show();

                    }
                }
            });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText password = findViewById(R.id.password_toggle1);
        EditText validPassword = findViewById(R.id.passwordValidation);
        EditText username = findViewById(R.id.userName1);
        EditText displayname = findViewById(R.id.displayName);



        PhotoProfilePreview = findViewById(R.id.profile_pic_imageview);
        PromtProfile = findViewById(R.id.newPic);

        api = RetrofitClient.getInstance().getMyApi();

        Button registerButton = findViewById(R.id.registerButton1);
        registerButton.setOnClickListener(v -> {

            display = displayname.getText().toString(); // nickname
            user = username.getText().toString();
            pass = password.getText().toString();
            int check = checkPassword(pass);
            String validPass = validPassword.getText().toString();
            // need to fix it and add check for empty fields
            if(user.isEmpty() || display.isEmpty() || pass.isEmpty() || validPass.isEmpty())
                username.setError("There are empty fields");
            else if (!(pass.equals(validPass)))
                password.setError("The passwords are not match!");
            else if(!(check == 1))
                password.setError("The passwords has to contain both letters and numbers");
            else {
               // first we find out if user exists
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("UserNameCheck", user );
                // need to somehow save img for each one.
                intent.putExtra("img", img);



                Launcher_UserCheck.launch(intent);




            }
        });




    }

    public int checkPassword(String password) {
        // if the password is all numbers
        if (password.matches("[0-9]+")) {
            return 0;
        }
        // if the password has no numbers
        if (password.matches("[a-zA-Z]+")) {
            return 0;
        }
        return 1;
    }

    public void UploadFile(View view) {

        // if not null it must mean we changed it from default pic.
        if(PhotoProfilePreview != null && PromtProfile.getVisibility() == View.VISIBLE) {
            PromtProfile.setVisibility(View.INVISIBLE);
        }
         Launcher.launch("image/*");

    }

    public void CreateUser(String user, String password, String nickname_default, String img) {

        String arguments[] = {user, password, img, nickname_default};
        Call<Void> call = RetrofitClient.getInstance().getMyApi().CreateUser(arguments);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                // TODO: need to check code 200 or something to indicate succesfull
              Response k = response;

              // TODO: need to find a way to save img for each contact - api does not support?

              // setting result of register to successful
              finish();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                finish();
            }
        });


       // create account here.



    }



}