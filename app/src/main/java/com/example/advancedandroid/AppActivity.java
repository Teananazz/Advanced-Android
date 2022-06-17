package com.example.advancedandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private String Token;
    private String Token_Bear; // Token with bear string before it.
    private String user;
    private Api api;
    private View EmptyIndicator;

    private List<Contact> Current_Contacts;
    private List<User> Users;
    private RecyclerView RecyclerView = null;
    private RecyclerView RecyclerViewMessages = null;
    private ContactAdapter Adapter;
    private MessageAdapter messageAdapter;
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


        api = RetrofitClient.getInstance().getMyApi();

        // Launcher for add contact activity.
        DefineLauncher();

        // make it visible only if no contacts
        EmptyIndicator = findViewById(R.id.tutorial);

        RecyclerView = findViewById(R.id.chats_recyclerview);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ContactDB")
                .allowMainThreadQueries()
                .build();

        contactDao = db.contactDao();

        Users = db.UserDao().index();

        if(Users == null || Users.isEmpty() ) {
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
        if(intent.hasExtra("Token")) {
            Token = intent.getStringExtra("Token");
            Token_Bear = "Bearer " + Token;
            user = intent.getStringExtra("User");
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

            Current_Contacts = contactDao.index(user);
            if(Current_Contacts == null) {
                Current_Contacts = new ArrayList<Contact>();
            }
            Adapter = new ContactAdapter(getApplicationContext(), Current_Contacts, Token_Bear, user, Users);
            RecyclerView.setAdapter(Adapter);
            RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));




            // meanwhile we checking server  in case there was additions.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getContacts(Token);
                    CheckUserList();
                }
            });
        }
        else {
            // if no Token then it is not possible to be at appactivity.
            finish();
        }
    }




    void getContacts(String Token) {

        Call<List<Contact>>  call = api.GetContacts(Token_Bear);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call , Response<List<Contact>> response) {

                List<Contact> ServerContacts = response.body();
                if(ServerContacts == null ) {
                    ServerContacts = new ArrayList<Contact>();
                }

                 if(ServerContacts.size() > 0) {

                    for(int  i = 0 ; i< ServerContacts.size() ; i++) {
                        ServerContacts.get(i).setUsernameOfLooker(user);
                    }
                }

                 if(Current_Contacts.isEmpty() && ServerContacts.size() == 1) {
                     int size = 0;
                     Current_Contacts.add(ServerContacts.get(0));
                     Adapter.notifyItemInserted(0);

                 }
                 // if we just added a contact
              else if(ServerContacts.size() == (Current_Contacts.size() + 1) ) {
                     int size = Current_Contacts.size();
                     int size2 = ServerContacts.size();
                     Current_Contacts.add(ServerContacts.get(size2 - 1));
                    Adapter.notifyItemInserted(size2 - 1);

                }
              else {
                     // this when we update potentially everyone if they changed info.
                      Current_Contacts = ServerContacts;
                      Adapter.notifyDataSetChanged();
                 }

               // we use Insert all because replace policy is replace in case details were updated.
                if(!Current_Contacts.isEmpty()) {
                    db.contactDao().InsertAll(Current_Contacts);
                }


                if (Current_Contacts.size() > 0 && EmptyIndicator.getVisibility() == View.VISIBLE) {
                            EmptyIndicator.setVisibility(View.INVISIBLE);
                }

                if( Current_Contacts.isEmpty() && EmptyIndicator.getVisibility() == View.INVISIBLE) {
                             // if no contacts then we show hint to add contact.
                    EmptyIndicator.setVisibility(View.VISIBLE);
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


    private void CheckUserList() {
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
                RecyclerView = findViewById(R.id.chats_recyclerview);
                Adapter = new ContactAdapter(getApplicationContext(), Current_Contacts, Token_Bear, user, Users);
                RecyclerView.setAdapter(Adapter);
                RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // In landscape
                    RecyclerViewMessages = findViewById(R.id.messages_recyclerview);
                    // just for check
                    List<Message> list = new List<Message>() {
                        @Override
                        public int size() {
                            return 0;
                        }

                        @Override
                        public boolean isEmpty() {
                            return false;
                        }

                        @Override
                        public boolean contains(@Nullable Object o) {
                            return false;
                        }

                        @NonNull
                        @Override
                        public Iterator<Message> iterator() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public Object[] toArray() {
                            return new Object[0];
                        }

                        @NonNull
                        @Override
                        public <T> T[] toArray(@NonNull T[] ts) {
                            return null;
                        }

                        @Override
                        public boolean add(Message message) {
                            return false;
                        }

                        @Override
                        public boolean remove(@Nullable Object o) {
                            return false;
                        }

                        @Override
                        public boolean containsAll(@NonNull Collection<?> collection) {
                            return false;
                        }

                        @Override
                        public boolean addAll(@NonNull Collection<? extends Message> collection) {
                            return false;
                        }

                        @Override
                        public boolean addAll(int i, @NonNull Collection<? extends Message> collection) {
                            return false;
                        }

                        @Override
                        public boolean removeAll(@NonNull Collection<?> collection) {
                            return false;
                        }

                        @Override
                        public boolean retainAll(@NonNull Collection<?> collection) {
                            return false;
                        }

                        @Override
                        public void clear() {

                        }

                        @Override
                        public Message get(int i) {
                            return null;
                        }

                        @Override
                        public Message set(int i, Message message) {
                            return null;
                        }

                        @Override
                        public void add(int i, Message message) {

                        }

                        @Override
                        public Message remove(int i) {
                            return null;
                        }

                        @Override
                        public int indexOf(@Nullable Object o) {
                            return 0;
                        }

                        @Override
                        public int lastIndexOf(@Nullable Object o) {
                            return 0;
                        }

                        @NonNull
                        @Override
                        public ListIterator<Message> listIterator() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public ListIterator<Message> listIterator(int i) {
                            return null;
                        }

                        @NonNull
                        @Override
                        public List<Message> subList(int i, int i1) {
                            return null;
                        }
                    };
                    list.add(new Message());
                    messageAdapter = new MessageAdapter(getApplicationContext(), list);
                    RecyclerViewMessages.setAdapter(messageAdapter);
                    RecyclerViewMessages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
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
