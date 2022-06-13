package com.example.advancedandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.advancedandroid.adapters.ContactAdapter;
import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.room.AppDB;
import com.example.advancedandroid.room.ContactDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private RecyclerView RecyclerView = null;
    private ContactAdapter Adapter;
    private AppDB db;
    private ContactDao contactDao;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_appscreen);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ContactDB")
                .allowMainThreadQueries()
                .build();

        contactDao = db.contactDao();

        FloatingActionButton addContactButton = findViewById(R.id.move_to_contactlist_fab);
        addContactButton.setOnClickListener(v -> {
            Intent i = new Intent(this, AddContactActivity.class);
            i.putExtra("Token", Token_Bear);
            startActivity(i);
        });

        contacts = contactDao.index();

        RecyclerView rvContacts = findViewById(R.id.chats_recyclerview);
        ContactAdapter adapter = new ContactAdapter(contacts);
        /*
        ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(this,
                android.R.layout.simple_list_item_1, contacts);
        */
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));


       Intent intent = getIntent();
       Token = intent.getStringExtra("Token");
       Token_Bear = "Bearer " + Token;
       user = intent.getStringExtra("User");
       api = RetrofitClient.getInstance().getMyApi();




       getContacts(Token);




       // now get his contacts.

      //TODO: need to create module to get contacts details and stuff.
        // TODO: create a function to get contacts.




        // make it visible only if no contacts
         EmptyIndicator = findViewById(R.id.tutorial);
        //  EmptyIndicator.setVisibility(View.INVISIBLE);
        // will later use it.


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
                         if (RecyclerView == null) {
                             RecyclerView = findViewById(R.id.chats_recyclerview);
                             Adapter = new ContactAdapter(getApplicationContext(), Current_Contacts, Token_Bear, user);
                             RecyclerView.setAdapter(Adapter);
                             RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                         }

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


}
