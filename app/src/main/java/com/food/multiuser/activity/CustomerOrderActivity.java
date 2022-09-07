package com.food.multiuser.activity;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Helper;
import com.food.multiuser.Model.Order;
import com.food.multiuser.R;
import com.food.multiuser.adapters.AdapterCustomerOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CustomerOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference orderReference;
    private List<Order> orderList;
    private AdapterCustomerOrder adapterOrders;
    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        initObjects();
        setData();
    }

    private void initObjects() {
        helper = new Helper(this);
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
                snapshot.getChildren().iterator().forEachRemaining(new Consumer<DataSnapshot>() {
                    @Override
                    public void accept(DataSnapshot dataSnapshot) {
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order.getUserId().equals(helper.getUser().getUid())) {
                            orderList.add(order);
                        }
                    }
                });
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setAdapter() {
        adapterOrders = new AdapterCustomerOrder(orderList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterOrders);
    }

}