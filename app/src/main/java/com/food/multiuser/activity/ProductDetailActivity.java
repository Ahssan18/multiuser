package com.food.multiuser.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Model.FeedBack;
import com.food.multiuser.Model.Product;
import com.food.multiuser.R;
import com.food.multiuser.adapters.AdapterFeedback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView title, description;
    private RecyclerView recycleFeedback;
    private Product product;
    private DatabaseReference feedbackRef;
    private List<FeedBack> feedBackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initObjects();
        clickListener();
        getProdectFeedback();
    }

    private void getProdectFeedback() {

        feedbackRef.child(product.getProductId()).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        snapshot.getChildren().iterator().forEachRemaining(new Consumer<DataSnapshot>() {
                            @Override
                            public void accept(DataSnapshot dataSnapshot) {
                                feedBackList.add(dataSnapshot.getValue(FeedBack.class));
                            }
                        });
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAdapter() {
        AdapterFeedback adapterFeedback = new AdapterFeedback(feedBackList);
        recycleFeedback.setLayoutManager(new LinearLayoutManager(this));
        recycleFeedback.setAdapter(adapterFeedback);
        adapterFeedback.notifyDataSetChanged();
    }

    private void clickListener() {
    }

    private void initObjects() {
        feedBackList = new ArrayList<>();
        feedbackRef = FirebaseDatabase.getInstance().getReference("feedback");
        product = getIntent().getParcelableExtra("post");
        title = findViewById(R.id.tv_title);
        description = findViewById(R.id.tv_description);
        recycleFeedback = findViewById(R.id.recycle_feedback);
        if (product != null) {
            title.setText(product.getName());
            description.setText(product.getDiscription());
        }


    }
}