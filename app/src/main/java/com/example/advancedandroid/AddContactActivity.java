package com.example.advancedandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Button backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            Intent i = new Intent(this, AppActivity.class);
            startActivity(i);
        });
    }
}