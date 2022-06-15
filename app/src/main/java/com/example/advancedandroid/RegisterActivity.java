package com.example.advancedandroid;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;


import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;

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


    EditText password;
    EditText validPassword;
    EditText username;
    EditText displayname;


    Api api = null;
    ActivityResultLauncher<String> LauncherImg;
    ActivityResultLauncher<Intent> Launcher_UserCheck;


    private Bitmap ImageSaved;
    private Bitmap check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        password = findViewById(R.id.password_toggle1);
        validPassword = findViewById(R.id.passwordValidation);
        username = findViewById(R.id.userName1);
        displayname = findViewById(R.id.displayName);


        defineImageLuncher();
        defineUserCheckLauncher();


        PhotoProfilePreview = findViewById(R.id.profile_pic_imageview);
        PromtProfile = findViewById(R.id.newPic);

        api = RetrofitClient.getInstance().getMyApi();

    }


    public void defineImageLuncher() {
        LauncherImg = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        //TODO: save String representation in data base when creating user.
                        // and Retrieve it later.
                        try {
                            ParcelFileDescriptor parcelFileDescriptor =
                                    getContentResolver().openFileDescriptor(result, "r");
                            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                            ImageSaved = BitmapFactory.decodeFileDescriptor(fileDescriptor);



                        }
                        catch(Exception e) {

                        }


                        PhotoProfilePreview.setImageBitmap(ImageSaved);

                        // encoding the image to BASE64 string so we can save it in database.
                        ByteArrayOutputStream ImageStream = new ByteArrayOutputStream();
                        ImageSaved.compress(Bitmap.CompressFormat.PNG, 100, ImageStream);

                        byte[] arr =  ImageStream.toByteArray();
                        img = Base64.encodeToString(arr, Base64.DEFAULT);

                        // how to decode img
                     //   byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                       // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                    }
                });

    }


    public void defineUserCheckLauncher() {
        Launcher_UserCheck = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        // 1 if  no such User exists.
                        if (result.getResultCode() == 1) {

                            CreateUser(user, pass, display, img);

                        }
                        // 2 if that user already exists
                        if (result.getResultCode() == 2) {
                            // there is probably a better way to show error.
                            Toast.makeText(getApplicationContext(), "This user name is already taken", Toast.LENGTH_LONG).show();

                        }
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
        if (PhotoProfilePreview != null && PromtProfile.getVisibility() == View.VISIBLE) {
            PromtProfile.setVisibility(View.INVISIBLE);
        }
        LauncherImg.launch("image/*");

    }

    public void CreateUser(String user, String password, String nickname_default, String img) {

        String arguments[] = {user, password, img, nickname_default};
        Call<Void> call = RetrofitClient.getInstance().getMyApi().CreateUser(arguments);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                // successful creation
                if(response.code() == 200) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()){
                                new AlertDialog.Builder(RegisterActivity.this)
                                        .setTitle("Successful Registration")
                                        .setMessage("Your account has been successfully created.")
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
                else {
                    if (!isFinishing()){
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("Failure")
                                .setMessage(" A failure has occured in the process")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                }

                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (!isFinishing()){
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Failure")
                            .setMessage(" A failure has occured in the process")
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


        // create account here.


    }


    public void AttemptRegister(View view) {


        display = displayname.getText().toString(); // nickname
        user = username.getText().toString();
        pass = password.getText().toString();
        int check = checkPassword(pass);
        String validPass = validPassword.getText().toString();
        // need to fix it and add check for empty fields
        if (user.isEmpty() || display.isEmpty() || pass.isEmpty() || validPass.isEmpty())
            username.setError("There are empty fields");
        else if (!(pass.equals(validPass)))
            password.setError("The passwords are not match!");
        else if (!(check == 1))
            password.setError("The passwords has to contain both letters and numbers");
        else {
            // first we find out if user exists
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("UserNameCheck", user);
            // need to somehow save img for each one.
           // intent.putExtra("img", img);

            Launcher_UserCheck.launch(intent);


        }
    }
}