package com.example.advancedandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AppActivity extends AppCompatActivity {
   private String Token;
   private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_appscreen);

       Intent intent = getIntent();
       Token = intent.getStringExtra("Token");
       user = intent.getStringExtra("User");

       // now get his contacts.

      //TODO: need to create module to get contacts details and stuff.
        // TODO: create a function to get contacts.




        // make it invisible only if no contacts
        View EmptyIndicator = findViewById(R.id.tutorial);
        //  EmptyIndicator.setVisibility(View.INVISIBLE);
        // will later use it.


    }

}
