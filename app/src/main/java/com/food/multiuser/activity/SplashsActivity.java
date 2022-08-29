package com.food.multiuser.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.food.multiuser.Helper;
import com.food.multiuser.R;
import com.google.firebase.auth.FirebaseAuth;


public class SplashsActivity extends AppCompatActivity {

    private ImageView logo;
    private static int splashTimeOut = 2000;
    private Helper helper;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashs);
        logo = (ImageView) findViewById(R.id.logo);
        helper = new Helper(this);
        auth = FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (auth.getCurrentUser() != null && helper.getUser() != null) {
                    if (helper.getUser().getUsertype() == 0) {
                        i = new Intent(SplashsActivity.this, HomeActivity.class);
                    } else {
                        i = new Intent(SplashsActivity.this, CustomerActivity.class);
                    }
                } else {
                    i = new Intent(SplashsActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, splashTimeOut);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mysplashanimation);

        logo.startAnimation(myanim);
    }
}