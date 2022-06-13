package com.example.advancedandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText password = findViewById(R.id.password_toggle1);
        EditText validPassword = findViewById(R.id.passwordValidation);
        final String pass = password.getText().toString();
        final String validPass = validPassword.getText().toString();
        int check = checkPassword(pass);

        Button registerButton = findViewById(R.id.registerButton1);
        registerButton.setOnClickListener(v -> {
            boolean passwords;
            passwords = (check == 1) && pass.equals(validPass);
            // need to fix it and add check for empty fields
            if (!passwords) {
                TextView checkPasswords = findViewById(R.id.checkPasswords);
                checkPasswords.setText("please fix the passwords");

            } else {
                // change this to the chats screen
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    public int checkPassword(String password) {
        if (password.isEmpty()) {
            return 0;
        }
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