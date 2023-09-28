package com.baishakhee.firbasewithrecyclerview.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baishakhee.firbasewithrecyclerview.MainActivity;
import com.baishakhee.firbasewithrecyclerview.R;
import com.baishakhee.firbasewithrecyclerview.databinding.ActivityLoginBinding;
import com.baishakhee.firbasewithrecyclerview.databinding.ActivitySplashBinding;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.userName.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                if (!isValidUsername(username)) {
                    Snackbar.make(v, "Invalid username ,Username (10 characters)", Snackbar.LENGTH_LONG).show();

                } else if (!isValidPassword(password)) {
                    Snackbar.make(v, "Invalid password, Password (7 characters, 1 uppercase, 1 special char, 1 numeric)", Snackbar.LENGTH_LONG).show();

                } else {
                    Snackbar.make(v, "Sign In Sucessfully", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("name", username);
                    startActivity(intent);
                }

            }
        });
    }

    private boolean isValidUsername(String username) {
        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_SHORT).show();

        }
        return username.length() == 10;
    }

    private boolean isValidPassword(String password) {
        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();

        }
        return password.length() == 7 &&           // 7 characters
                password.matches(".*[A-Z].*") &&   // At least 1 uppercase letter
                password.matches(".*\\d.*") &&     // At least 1 numeric digit
                password.matches(".*[@#$%^&+=].*"); // At least 1 special character
    }
}