package com.example.advancedandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advancedandroid.adapters.ContactAdapter;
import com.example.advancedandroid.adapters.MessageAdapter;
import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.models.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingActivity extends AppCompatActivity {
    private String Token_bear;
    private Intent intent;
    private String Nickname;
    private String UserNameContact;
    private View[] views;
    private RecyclerView messages;
    private List<Message> MessageList;
    private Api api;
    private MessageAdapter Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        intent = getIntent();

        Token_bear = intent.getStringExtra("Token");
        Nickname = intent.getStringExtra("Nickname");
        UserNameContact = intent.getStringExtra("UserName");
        views = new View[]{findViewById(R.id.receiver_name), findViewById(R.id.profile_pic_imageview),  findViewById(R.id.msg_recyclerview) };
         TextView name = (TextView)views[0];
          name.setText(Nickname);
          messages = (RecyclerView)views[2];

         api = RetrofitClient.getInstance().getMyApi();

          getMessages(Token_bear, UserNameContact);

    }

    void getMessages(String Token, String UserNameContact) {

        Call<List<Message>> call = api.getMessages(Token, UserNameContact);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {

                List<Message> entry = response.body();


               MessageList = response.body();

                 if(MessageList!=null) {

                     if( (entry!=null && entry.size() > MessageList.size()) && Adapter!=null) {
                            Adapter.notifyDataSetChanged();
                     }

                    if(Adapter == null) {
                        Adapter = new MessageAdapter(getApplicationContext(), MessageList);
                        messages.setAdapter(Adapter);
                        messages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    }

                 }

            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });








    }


    public void SendMessage(View view) {

       TextView MessageView = findViewById(R.id.typing_space);
       String Message= MessageView.getText().toString();

       Call <String> call = api.SendMessage(Token_bear, Message, UserNameContact);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call , Response<String> response) {
                // need to test it out.
                getMessages(Token_bear, UserNameContact);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });




    }
}
