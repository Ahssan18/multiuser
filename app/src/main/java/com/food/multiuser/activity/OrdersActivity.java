package com.food.multiuser.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Helper;
import com.food.multiuser.Model.CartItem;
import com.food.multiuser.Model.Order;
import com.food.multiuser.Model.Product;
import com.food.multiuser.R;
import com.food.multiuser.adapters.AdapterPlaceOrder;
import com.food.multiuser.demo.ScanProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements View.OnClickListener {

    static RecyclerView recyclerview;
    LinearLayoutManager layoutManager;
    private DatabaseReference reference, orderRef, cartReference;
    private List<Product> listCard;
    private AdapterPlaceOrder AdapterPlaceOrder;
    private FirebaseAuth auth;
    private String TAG = "OrdersActivity";
    private Button btnPlaceOrder, btnAddProduct;
    private Helper helper;
    private TextView totalAmout;
    private CartItem cartItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        initObjects();
        clickListener();
        setData();
    }

    private void setData() {
        reference.child("MyCart").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cartItem = (snapshot.getValue(CartItem.class));
                    Log.e(TAG, "CARTDATA" + cartItem.toString());
                    totalAmout.setText(cartItem.getTotalPrice() + "");
                    listCard.addAll(cartItem.getList());
                    setAdapter();
                    AdapterPlaceOrder.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickListener() {
        btnPlaceOrder.setOnClickListener(this);
        btnAddProduct.setOnClickListener(this);
    }

    private void initObjects() {
        btnAddProduct = findViewById(R.id.btnscanmoreproducts);
        totalAmout = findViewById(R.id.total);
        helper = new Helper(this);
        reference = FirebaseDatabase.getInstance().getReference();
        orderRef = FirebaseDatabase.getInstance().getReference("order");
        cartReference = FirebaseDatabase.getInstance().getReference("MyCart");
        auth = FirebaseAuth.getInstance();
        btnPlaceOrder = findViewById(R.id.btnplaceorder);
        layoutManager = new LinearLayoutManager(this);
        listCard = new ArrayList<>();
        recyclerview = findViewById(R.id.recyclerview_order);
    }

    private void setAdapter() {
        AdapterPlaceOrder = new AdapterPlaceOrder(this, listCard);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(AdapterPlaceOrder);
    }

    @Override
    public void onClick(View view) {
        if (view == btnPlaceOrder) {
            if (cartItem != null) {
                Order order = new Order();
                order.setUserId(helper.getUser().getUid());
                order.setTotalPrice(cartItem.getTotalPrice() + "");
                order.setDeliverStatus(false);
                order.setAcceptStatus(false);
                order.setProductList(listCard);
                String key = orderRef.push().getKey();
                order.setOrderId(key);
                orderRef.child(key).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cartReference.child(helper.getUser().getUid()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                listCard.clear();
                                AdapterPlaceOrder.notifyDataSetChanged();
                                finish();
                                Toast.makeText(OrdersActivity.this, "Order place successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }


        } else if (view == btnAddProduct) {
            startActivity(new Intent(this, ScanProduct.class));
        }
    }
}