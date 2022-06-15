package com.example.advancedandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.advancedandroid.adapters.ContactAdapter;
import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.models.User;
import com.example.advancedandroid.room.AppDB;
import com.example.advancedandroid.room.ContactDao;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppActivity extends AppCompatActivity {
   private String Token;
   private String Token_Bear; // Token with bear string before it.
   private String user;
   private Api api;
   private View EmptyIndicator;

   private List<Contact> Current_Contacts;
   private List<User> Users;
    private RecyclerView RecyclerView = null;
    private ContactAdapter Adapter;
    private AppDB db;
    private ContactDao contactDao;
    private List<Contact> contacts;
    private ContactAdapter contactAdapter;

    ActivityResultLauncher<Intent> Launcher;

    // This is launcher for contact adding screen - so we can know the added contact.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_appscreen);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(AppActivity.this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
        });

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ContactDB")
                .allowMainThreadQueries()
                .build();

        CheckUserList();
        DefineLauncher();





       Intent intent = getIntent();
       Token = intent.getStringExtra("Token");
       Token_Bear = "Bearer " + Token;
       user = intent.getStringExtra("User");
       api = RetrofitClient.getInstance().getMyApi();




       getContacts(Token);


        // why you need this? check later
        contactDao = db.contactDao();
        contacts = contactDao.index();
        RecyclerView rvContacts = findViewById(R.id.chats_recyclerview);
        contactAdapter = new ContactAdapter(contacts, Users);
        rvContacts.setAdapter(contactAdapter);
        // TODO fix the problem with the line under me
        //rvContacts.setLayoutManager(new LinearLayoutManager(this));


        // make it visible only if no contacts
         EmptyIndicator = findViewById(R.id.tutorial);



    }

    @Override
    protected void onResume() {
        super.onResume();
        contacts.clear();
        contacts.addAll(contactDao.index());
        contactAdapter.notifyDataSetChanged();
    }

    void getContacts(String Token) {

        Call<List<Contact>>  call = api.GetContacts(Token_Bear);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call , Response<List<Contact>> response) {

                 if(RecyclerView != null ) {

                     if(Current_Contacts.size() < response.body().size()) {
                         Current_Contacts = response.body();
                         Adapter.notifyDataSetChanged();
                     }

                 }
              else {
                     Current_Contacts = response.body();
                     if (Current_Contacts != null) {
                         // we start caring about recycler view when there is contacts to show.
                       //  RecyclerView = findViewById(R.id.chats_recyclerview);
                        // Adapter = new ContactAdapter(getApplicationContext(), Current_Contacts, Token_Bear, user, Users);
                       //  RecyclerView.setAdapter(Adapter);
                        // RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                         // first we take down the add contact promt.
                         if (EmptyIndicator.getVisibility() == View.VISIBLE) {
                             EmptyIndicator.setVisibility(View.INVISIBLE);
                         }


                     } else {
                         // if no contacts then we show hint to add contact.
                         if (EmptyIndicator.getVisibility() == View.INVISIBLE) {
                             EmptyIndicator.setVisibility(View.VISIBLE);
                         }

                     }

                 }


            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });





    }

     void DefineLauncher() {

         Launcher = registerForActivityResult(
                 new ActivityResultContracts.StartActivityForResult(),
                 new ActivityResultCallback<ActivityResult>() {
                     @Override
                     public void onActivityResult(ActivityResult result) {

                         // 1 he just returned back and nothing happend.
                         if (result.getResultCode() == 1) {


                         }
                         if(result.getResultCode() == 2) {
                             Intent res = result.getData();
                             String user = res.getStringExtra("username");
                             String nickname =res.getStringExtra("nickname");
                             String serv = res.getStringExtra("serv");
                             Contact entry = new Contact(user, nickname ,serv, "", "");
                             Current_Contacts.add(entry);
                             Adapter.notifyItemInserted(Current_Contacts.size() - 1);

                         }
                     }
                 });
     }


    public void AddContactTransfer(View view) {

            Intent intent = new Intent(this, AddContactActivity.class);
        intent.putExtra("Token", Token_Bear);
        intent.putExtra("UserHost", user);
            Launcher.launch(intent);
    }


    private void CheckUserList() {
        Call<List<User>> call = RetrofitClient.getInstance().getMyApi().getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                 Users = response.body();
                RecyclerView = findViewById(R.id.chats_recyclerview);
                Adapter = new ContactAdapter(getApplicationContext(), Current_Contacts, Token_Bear, user, Users);
                RecyclerView.setAdapter(Adapter);
                RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();

            }

        });
    }
}
