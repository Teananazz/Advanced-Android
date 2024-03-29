package com.example.advancedandroid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.room.AppDB;
import com.example.advancedandroid.room.ContactDao;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactActivity extends AppCompatActivity {

    private AppDB db;
    private ContactDao contactDao;
    private String Bear_Token;
    private Api my_api;
    String HostUser;
    EditText userName;
    EditText nickName;
    EditText server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ContactDB")
                .allowMainThreadQueries()
                .build();

        contactDao = db.contactDao();

        my_api = RetrofitClient.getInstance().getMyApi();


       userName = findViewById(R.id.userName2);
        nickName = findViewById(R.id.displayName1);
        server = findViewById(R.id.serverAddress);

        // This is intention we get from AppActivity
        Intent intention = getIntent();
        Bear_Token = intention.getStringExtra("Token");
        HostUser = intention.getStringExtra("UserHost");

    }

  void AddContact(String user, String nickname, String serv) {

         String arguments[] = {user, nickname ,serv};


       Call<Void> call = RetrofitClient.getInstance().getMyApi().AddContact(Bear_Token,arguments);

       call.enqueue(new Callback<Void>() {


           @Override
           public void onResponse(Call<Void> call, Response<Void> response) {


               SendInvitation(HostUser, user,serv);
             // do nothing as we expect it to work. maybe later change.

           }

           @Override
           public void onFailure(Call<Void> call, Throwable t) {

           }

       });


  }

  void SendInvitation(String HostUser, String TargetUser, String serv) {

      String server = serv;
      Api custom = null;

      if(server.contains("localhost")) {
          server = server.replace("localhost", "10.0.0.2");

      }
      // if it is not equal to Base url, we have to create another retrofit object.
      if(!server.equals("10.0.0.2:7179") ) {

          RetrofitClient.getInstance().AddClient("https://".concat(server).concat("/api/"));
          custom =  RetrofitClient.getInstance().getCustom_Api();

      }

      String arguments[] = {HostUser, TargetUser, serv};

      Call<Void> call;
      // will only enter custom if different server address than user.
      if( custom != null) {
          call = custom.SendInvitation(arguments);
      }
      else {
          call = my_api.SendInvitation(arguments);
      }


      call.enqueue(new Callback <Void>() {
          @Override
          public void onResponse(Call <Void> call, Response <Void> response) {

              Response k = response;

          }

          @Override
          public void onFailure(Call <Void> call, Throwable t) {
              Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
              Log.d("Error222: ", t.toString());
          }

      });


  }


    public void AddContactBtn(View view) {


        String user = userName.getText().toString();
        String nickname = nickName.getText().toString();
        String serv = server.getText().toString();

        // api does not contain Host user field.
        Contact contact = new Contact(user,
                nickname, serv,"", "", HostUser);

        contactDao.insert(contact);


      ///  Intent intention = getIntent();
      //  Bear_Token = intention.getStringExtra("Token");
      //  HostUser = intention.getStringExtra("UserHost");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AddContact(user, nickname ,serv);
                Intent intent = new Intent();
                intent.putExtra("username", user);
                intent.putExtra("nickname", nickname);
                intent.putExtra("serv", serv);
                setResult(2, intent);
                finish();
            }
        });




    }

    public void BackButtonActivate(View view) {
        Intent intent = new Intent();
        setResult(1, intent);
        finish();

    }
}