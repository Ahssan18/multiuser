package com.food.multiuser.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.food.multiuser.Model.FeedBack;
import com.food.multiuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedBackActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText etMessage;
    private Button btnSubmit;
    private DatabaseReference feedbackReference;
    private String productId, name;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initObject();
        clickListener();
    }

    private void clickListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedBack feedBack = new FeedBack();
                feedBack.setRating(ratingBar.getRating());
                String message = etMessage.getText().toString();
                if (!message.equals("")) {
                    feedBack.setMessage(etMessage.getText().toString());

                    feedbackReference.child(productId).push().setValue(feedBack).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(FeedBackActivity.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void initObject() {
        ratingBar = findViewById(R.id.ratingBar);
        tvTitle = findViewById(R.id.tv_title);
        etMessage = findViewById(R.id.et_feedback_msg);
        btnSubmit = findViewById(R.id.btn_submit_feedback);
        productId = getIntent().getStringExtra("productId");
        name = getIntent().getStringExtra("name");
        tvTitle.setText("Feedback about " + name);
        feedbackReference = FirebaseDatabase.getInstance().getReference("feedback");

    }
}