package com.food.multiuser.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Model.Order;
import com.food.multiuser.R;
import com.food.multiuser.adapters.AdapterOrders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference orderReference;
    private List<Order> orderList;
    private AdapterOrders adapterOrders;
    private String TAG = "OrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initObjects();
        setData();
    }

    private void initObjects() {
        orderList = new ArrayList<>();
        getSupportActionBar().setTitle("Order");
        recyclerView = findViewById(R.id.recycleOrders);
        orderReference = FirebaseDatabase.getInstance().getReference("order");
    }

    private void setData() {
        orderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    snapshot.getChildren().iterator().forEachRemaining(new Consumer<DataSnapshot>() {
                        @Override
                        public void accept(DataSnapshot dataSnapshot) {
                            orderList.add(dataSnapshot.getValue(Order.class));
                        }
                    });
                    Log.e(TAG, "orderList " + orderList.toString());
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setAdapter() {
        adapterOrders = new AdapterOrders(orderList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterOrders);
        adapterOrders.notifyDataSetChanged();
    }
}