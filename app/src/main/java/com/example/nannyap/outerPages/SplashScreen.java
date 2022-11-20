package com.example.nannyap.outerPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.nannyap.R;
import com.example.nannyap.nanny.NannyActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (FirebaseAuth.getInstance().getUid() != null){
                    startActivity(new Intent(SplashScreen.this, NannyActivity.class));
                    finish();
                }else{*/
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                //}

            }
        },2000);
    }
}