package com.example.advancedandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ContactDB")
                .allowMainThreadQueries()
                .build();

        contactDao = db.contactDao();

        Button addContactButton = findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(v -> {
            EditText userName = findViewById(R.id.userName2);
            EditText nickName = findViewById(R.id.displayName1);
            EditText server = findViewById(R.id.serverAddress);
            Contact contact = new Contact(userName.getText().toString(),
                    nickName.getText().toString(), server.getText().toString());

            String user = userName.getText().toString();
            String nickname = nickName.getText().toString();
            String serv = server.getText().toString();
            Intent intention = getIntent();
            Bear_Token = intention.getStringExtra("Token");
            AddContact(user, nickname ,serv);

            contactDao.insert(contact);

            finish(); // finishes activity - maybe should take it out.
        });

        Button backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            Intent i = new Intent(this, AppActivity.class);
            startActivity(i);
        });
    }

  void AddContact(String user, String nickname, String serv) {

         String arguments[] = {user, nickname ,serv};


       Call<Void> call = RetrofitClient.getInstance().getMyApi().AddContact(Bear_Token,arguments);

       call.enqueue(new Callback<Void>() {

           // TODO: need to somehow call after this to getContacts so that it won't look too stupid
           @Override
           public void onResponse(Call<Void> call, Response<Void> response) {




           }

           @Override
           public void onFailure(Call<Void> call, Throwable t) {

           }

       });


  }
}