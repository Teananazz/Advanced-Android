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
import com.example.advancedandroid.models.SendStringAsObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

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
        views = new View[]{findViewById(R.id.receiver_name), findViewById(R.id.profile_pic_imageview), findViewById(R.id.msg_recyclerview)};
        TextView name = (TextView) views[0];
        name.setText(Nickname);
        messages = (RecyclerView) views[2];

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

                if (MessageList != null) {

                    if ((entry != null && entry.size() > MessageList.size()) && Adapter != null) {
                        Adapter.notifyDataSetChanged();
                    }

                    if (Adapter == null) {
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


    public void SendMessage(View view)  {







        TextView f = findViewById(R.id.typing_space);
          String message = f.getText().toString();
        //TODO: Massive problem - GSON Convertor can only be activated second - but the first convertor
        // is an invalid send type - because asp.net awaits for a JSON!!! and not text What do??
        // Two Options: 1. Get Two Different Retrofit client types.
        //              2. Get Custom Converter
        //               3. Find Way Bypass that since only one request
        // Attempt number 1: Use an object

        SendStringAsObject messageObject = new SendStringAsObject(message);



        String body = "plain text request body";


        Call <Void>  call = api.PostMessages(Token_bear, UserNameContact,  message);
        call.enqueue(new Callback <Void>() {
            @Override
            public void onResponse(Call <Void> call, Response <Void> response) {

                f.setText("");

                getMessages(Token_bear, UserNameContact);


            }

            @Override
            public void onFailure(Call <Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });


    }
}

