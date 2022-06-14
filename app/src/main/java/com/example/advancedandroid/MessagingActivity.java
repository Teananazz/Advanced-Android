package com.example.advancedandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.advancedandroid.adapters.MessageAdapter;
import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.Message;
import com.example.advancedandroid.room.AppDB;
import com.example.advancedandroid.room.MessageDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingActivity extends AppCompatActivity {
    private String Token_bear;
    private Intent intent;
    private String Nickname;
    private String HostUserName;
    private String UserNameContact;
    private String ContactServer;
    private View[] views;
    private RecyclerView messages;
    private List<Message> MessageList;
    private Api api;
    private MessageAdapter Adapter;
    private AppDB db;
    private MessageDao messageDao;
    private List<Message> messagesList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        intent = getIntent();
        Token_bear = intent.getStringExtra("Token");
        Nickname = intent.getStringExtra("Nickname");
        UserNameContact = intent.getStringExtra("UserName");
        HostUserName = intent.getStringExtra("HostUser");
        ContactServer = intent.getStringExtra("Server");

        views = new View[]{findViewById(R.id.receiver_name), findViewById(R.id.profile_pic_imageview), findViewById(R.id.msg_recyclerview)};
        TextView name = (TextView) views[0];
        name.setText(Nickname);
        messages = (RecyclerView) views[2];

        api = RetrofitClient.getInstance().getMyApi();


        getMessages(Token_bear, UserNameContact, 0);


        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "MessageDB")
                .allowMainThreadQueries()
                .build();
        messageDao = db.messageDao();
        messagesList = messageDao.index();

        
        RecyclerView rvMessages = findViewById(R.id.msg_recyclerview);
        messageAdapter = new MessageAdapter(messagesList);
        rvMessages.setAdapter(messageAdapter);
        // TODO fix the problem with the line under me
        //rvMessages.setLayoutManager(new LinearLayoutManager(this));




    }

    void getMessages(String Token, String UserNameContact, int flagChanged) {

        Call<List<Message>> call = api.getMessages(Token, UserNameContact);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {

                List<Message> entry = response.body();

                // for now we get all the fields from database.
                 if(flagChanged == 1) {
                     MessageList.add(entry.get(entry.size() - 1));
                     Adapter.notifyItemChanged(MessageList.size() - 1);
                     messages.scrollToPosition(Adapter.getItemCount()-1);
                 }
                 else {
                     MessageList = response.body();

                         if (Adapter == null ) {

                         Adapter = new MessageAdapter(getApplicationContext(), MessageList);
                         messages.setAdapter(Adapter);
                         messages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                     }

                     MessageList = response.body();

                 }

            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });


    }


    public void SendMessage(View view)  {

        TextView f = findViewById(R.id.typing_space);
          String message = f.getText().toString();

       Call <Void>  call = api.PostMessages(Token_bear, UserNameContact,  message);
        call.enqueue(new Callback <Void>() {
            @Override
            public void onResponse(Call <Void> call, Response <Void> response) {

                f.setText("");

                getMessages(Token_bear, UserNameContact, 1);

               SendTransferRequest(HostUserName , UserNameContact, message, ContactServer);


            }

            @Override
            public void onFailure(Call <Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });


    }


    public void SendTransferRequest(String from, String to, String message, String server) {

        String serv = server;
        Api custom = null;

        if(server.contains("localhost")) {
            server = server.replace("localhost", "10.0.0.2");

        }
        // if it is not equal to Base url, we have to create another retrofit object.
        if(!server.equals("10.0.0.2:7179") ) {

           RetrofitClient.getInstance().AddClient("https://".concat(server).concat("/api/"));
            custom =  RetrofitClient.getInstance().getCustom_Api();


        }

        String arr[] ={ from, to, message};
        Call<Void> call;
        // will only enter custom if different server address than user.
        if( custom != null) {
            call = custom.SendTransfer(arr);
        }
        else {
            call = api.SendTransfer(arr);
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
}

