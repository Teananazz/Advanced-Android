package com.example.advancedandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText password = findViewById(R.id.password_toggle1);
        EditText validPassword = findViewById(R.id.passwordValidation);
        EditText username = findViewById(R.id.userName1);
        EditText displayname = findViewById(R.id.displayName);
        String display = displayname.getText().toString();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String validPass = validPassword.getText().toString();

        int check = checkPassword(pass);
        Button registerButton = findViewById(R.id.registerButton1);
        registerButton.setOnClickListener(v -> {
            // need to fix it and add check for empty fields
            if(user.isEmpty() || display.isEmpty() || pass.isEmpty() || validPass.isEmpty())
                username.setError("There are empty fields");
            else if (!(pass.equals(validPass)))
                password.setError("The passwords are not match!");
            else if(!(check == 1))
                password.setError("The passwords has to contain both letters and numbers");
            else {
                // change this to the chats screen
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    public int checkPassword(String password) {
        // if the password is all numbers
        if (password.matches("[0-9]+")) {
            return 0;
        }
        // if the password has no numbers
        if (password.matches("[a-zA-Z]+")) {
            return 0;
        }
        return 1;
    }

}