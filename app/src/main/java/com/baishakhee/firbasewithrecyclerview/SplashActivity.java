package com.baishakhee.firbasewithrecyclerview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.baishakhee.firbasewithrecyclerview.view.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;


import com.baishakhee.firbasewithrecyclerview.databinding.ActivitySplashBinding;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    final int delayMillis = 1000; // 10 seconds in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to navigate to the next activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to it
            }
        }, delayMillis);

    }

}