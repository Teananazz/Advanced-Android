package com.example.advancedandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.room.AppDB;
import com.example.advancedandroid.room.ContactDao;

public class AddContactActivity extends AppCompatActivity {

    private AppDB db;
    private ContactDao contactDao;

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
            contactDao.insert(contact);

            finish();
        });

        Button backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            Intent i = new Intent(this, AppActivity.class);
            startActivity(i);
        });
    }
}