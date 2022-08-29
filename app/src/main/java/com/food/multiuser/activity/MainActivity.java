package com.food.multiuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.food.multiuser.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
    public void login (View view)
    {
        startActivity(new Intent(this,LoginActivity.class));
    }
    public void register (View view)
    {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}