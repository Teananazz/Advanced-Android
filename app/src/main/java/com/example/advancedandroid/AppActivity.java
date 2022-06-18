package com.example.advancedandroid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Dao;
import androidx.room.Room;

import com.example.advancedandroid.adapters.ContactAdapter;
import com.example.advancedandroid.adapters.MessageAdapter;
import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.models.Message;
import com.example.advancedandroid.models.User;
import com.example.advancedandroid.room.AppDB;
import com.example.advancedandroid.room.ContactDao;
import com.example.advancedandroid.room.MessageDao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppActivity extends AppCompatActivity {

    // for now they are all public since we access them in other fragments.
    // later will probably change to have getters setters.
    public String Token;
    public String Token_Bear; // Token with bear string before it.
    public String user;
    public Api api;
    public View EmptyIndicator;

    public List<Contact> Current_Contacts;
    public List<User> Users;
    public RecyclerView RecyclerView = null;
    public RecyclerView RecyclerViewMessages = null;
    public ContactAdapter Adapter;
    public MessageAdapter AdapterMess;
    public AppDB db;
    public AppDB dbMess;

    public ContactDao contactDao;
    public List<Contact> contacts;
    public ContactAdapter contactAdapter;
    public MessageAdapter messageAdapter;


    // this is for Message/Contact adapter difference.
    public String UserChoosen;
    public String NickNameChoose;
    public String ServerChosen;
    public List<Message> messageList;


    // this is not null if a message was sent.
    public String messageSend = null;

    public String Orientation;




    public BroadcastReceiver NotificationGetter;
    public ActivityResultLauncher<Intent> Launcher;
    public Bitmap imgContact;








    // This is launcher for contact adding screen - so we can know the added contact.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_appscreen);

        api = RetrofitClient.getInstance().getMyApi();

        // Launcher for add contact activity.
        DefineLauncher();





        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ContactDB")
                .allowMainThreadQueries()
                .build();
         dbMess = Room.databaseBuilder(getApplicationContext(), AppDB.class, "MessageDB")
                .allowMainThreadQueries()
                .build();
        contactDao = db.contactDao();

        Users = db.UserDao().index();


        if (Users == null || Users.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Users = new ArrayList<User>();
                    // if no accounts then it is not possible to get here unless UserDao is not updated.
                    CheckUserList();
                }
            });

        }


        Intent intent = getIntent();
        if (intent.hasExtra("Token")) {

            Token = intent.getStringExtra("Token");
            Token_Bear = "Bearer " + Token;
            user = intent.getStringExtra("User");

              Current_Contacts = contactDao.index(user);
            // null only on first time.
            if (savedInstanceState == null) {


                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Orientation="LANDSCAPE";
                    // In landscape
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fragment_container_view2, ContactsFragment.class, null)
                            .commit();


                    // in the beginning the second container is empty ( when no contacts has been clicked on).
                    // this is changed when click in contact adapter.

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fragment_container_view, EmptyFragment.class, null)
                            .commit();

                } else {
                    Orientation="PORTRAIT";
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fragment_container_view, ContactsFragment.class, null)
                            .commit();
                    // In portrait
                }

            }

            // we get firebase token.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    // Get new FCM registration token
                                    String token = task.getResult();
                                    //  ((TextView)findViewById(R.id.Title)).setText(token);

                                    JoinFireBase(user, token);
                                }
                            });
                }
            });

        }
        else {
            finish();
        }


          //  Current_Contacts = contactDao.index(user);
       //     if(Current_Contacts == null) {
       //         Current_Contacts = new ArrayList<Contact>();
       //  }
         //   Adapter = new ContactAdapter(getApplicationContext(), Current_Contacts, Token_Bear, user, Users);
        //    RecyclerView.setAdapter(Adapter);
          //  RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



//
//            // meanwhile we checking server  in case there was additions.
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    getContacts(Token);
//                    CheckUserList();
//                }
//            });
//        }
//        else {
//            // if no Token then it is not possible to be at appactivity.
//            finish();
//        }
        }





//    void getContacts(String Token) {
//
//        Call<List<Contact>>  call = api.GetContacts(Token_Bear);
//        call.enqueue(new Callback<List<Contact>>() {
//            @Override
//            public void onResponse(Call<List<Contact>> call , Response<List<Contact>> response) {
//
//                List<Contact> ServerContacts = response.body();
//                if(ServerContacts == null ) {
//                    ServerContacts = new ArrayList<Contact>();
//                }
//
//                 if(ServerContacts.size() > 0) {
//
//                    for(int  i = 0 ; i< ServerContacts.size() ; i++) {
//                        ServerContacts.get(i).setUsernameOfLooker(user);
//                    }
//                }
//
//                 if(Current_Contacts.isEmpty() && ServerContacts.size() == 1) {
//                     int size = 0;
//                     Current_Contacts.add(ServerContacts.get(0));
//                     Adapter.notifyItemInserted(0);
//
//                 }
//                 // if we just added a contact
//              else if(ServerContacts.size() == (Current_Contacts.size() + 1) ) {
//                     int size = Current_Contacts.size();
//                     int size2 = ServerContacts.size();
//                     Current_Contacts.add(ServerContacts.get(size2 - 1));
//                    Adapter.notifyItemInserted(size2 - 1);
//
//                }
//              else {
//                     // this when we update potentially everyone if they changed info.
//                     Current_Contacts.clear();
//                     Current_Contacts.addAll(ServerContacts);
//                      Adapter.notifyDataSetChanged();
//                 }
//
//               // we use Insert all because replace policy is replace in case details were updated.
//                if(!Current_Contacts.isEmpty()) {
//                    db.contactDao().InsertAll(Current_Contacts);
//                }
//
//                if (Current_Contacts.size() > 0 && EmptyIndicator.getVisibility() == View.VISIBLE) {
//                            EmptyIndicator.setVisibility(View.INVISIBLE);
//                }
//
//                if( Current_Contacts.isEmpty() && EmptyIndicator.getVisibility() == View.INVISIBLE) {
//                             // if no contacts then we show hint to add contact.
//                    EmptyIndicator.setVisibility(View.VISIBLE);
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Contact>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
//                Log.d("Error222: ", t.toString());
//            }
//
//        });
//
//
//
//
//
//    }

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
                             String username = res.getStringExtra("username");
                             String nickname =res.getStringExtra("nickname");
                             String serv = res.getStringExtra("serv");
                             Contact entry = new Contact(username, nickname ,serv, "", "", user);
                             if(Current_Contacts == null) {
                                 Current_Contacts = new ArrayList<Contact>();
                             }
                             Current_Contacts.add(entry);
                             contactDao.insert(entry);
                             Adapter.notifyItemInserted(Current_Contacts.size() - 1);
                             if(EmptyIndicator.getVisibility() == View.VISIBLE) {
                                 EmptyIndicator.setVisibility(View.INVISIBLE);
                             }

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


          public void CheckUserList() {
        Call<List<User>> call =api.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {


                List<User> res = response.body();
                if(res == null) {
                    res = new ArrayList<User>();
                }

               if(Users.size() < res.size()) {
                   Users = res;

               }

               if(Users.size() > 0) {
                   // because of policy of replacement only on certain cases we don't have to worry.
                   // also we might replace because there could be updates on img and stuff.
                   db.UserDao().insertAll(res);
               }


                Users = response.body();
               // RecyclerView = findViewById(R.id.chats_recyclerview);
             //   Adapter = new ContactAdapter(getApplicationContext(), Current_Contacts, Token_Bear, user, Users);
               // RecyclerView.setAdapter(Adapter);
               // RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();

            }
        });
    }

    // we'll make them static in case we need to access them from other location.
    public static void JoinFireBase(String user, String token) {
        String arr[] = {user, token};
        Call <Void> call = RetrofitClient.getInstance().getMyApi().JoinFireBase(arr);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Response<Void> k = response;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }

        });


    }
    public static void LeaveFireBase(String user) {
     Call <Void> call = RetrofitClient.getInstance().getMyApi().LeaveFireBase(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Response<Void> k = response;

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }

        });

    }


    // when this activity is destroyed the user obviously not in firebase to get notifications.
    @Override
    public void onDestroy() {
        super.onDestroy();
        LeaveFireBase(user);
    }
}
