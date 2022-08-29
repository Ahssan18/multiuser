package com.food.multiuser.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.food.multiuser.Helper;
import com.food.multiuser.Model.User;
import com.food.multiuser.R;
import com.food.multiuser.demo.ScanProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private Button Login;
    private TextView passwordreset, register;
    private EditText passwordresetemail;
    private ProgressBar progressBar;
    private Helper helper;

    private FirebaseAuth auth;
    private ProgressDialog processDialog;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText) findViewById(R.id.emailSignIn);
        Password = (EditText) findViewById(R.id.password);
        Login = (Button) findViewById(R.id.Login);
        helper = new Helper(this);
        passwordreset = findViewById(R.id.forgotpassword);
        passwordresetemail = findViewById(R.id.emailSignIn);
        register = findViewById(R.id.register);
        progressBar = (ProgressBar) findViewById(R.id.progressbars);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
        processDialog = new ProgressDialog(this);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Email.getText().toString(), Password.getText().toString());
            }

        });
        passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpasword();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void resetpasword() {
        final String resetemail = passwordresetemail.getText().toString();

        if (resetemail.isEmpty()) {
            passwordresetemail.setError("It's empty");
            passwordresetemail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(resetemail)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void validate(String userEmail, String userPassword) {

        processDialog.setMessage("................Please Wait.............");
        processDialog.show();

        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    processDialog.dismiss();

                    String uid = task.getResult().getUser().getUid();
                    Log.e(TAG, "onComplete" + FirebaseDatabase.getInstance().getReference("Users"));
                    FirebaseDatabase.getInstance().getReference("Users").child(uid).child("UserDetails")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    helper.saveUser(user);
                                    Toast.makeText(LoginActivity.this, "Login Sucessfull!!!", Toast.LENGTH_SHORT).show();
                                    Intent intent;
                                    if (user.getUsertype() == 0) {
                                        intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    } else {
                                        intent = new Intent(LoginActivity.this, CustomerActivity.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    processDialog.dismiss();
                }
            }
        });
    }
}