package com.food.multiuser.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.food.multiuser.R;
import com.food.multiuser.adapters.AdapterCart;
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
import java.util.function.Consumer;

public class OrdersActivity extends AppCompatActivity implements View.OnClickListener {

    static RecyclerView recyclerview;
    LinearLayoutManager layoutManager;
    private DatabaseReference reference, orderRef, cartReference;
    private List<CartItem> listCard;
    private AdapterCart adapterCart;
    private FirebaseAuth auth;
    private String TAG = "OrdersActivity";
    private Button btnPlaceOrder, btnAddProduct;
    private Helper helper;
    private TextView totalAmout;


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
                if (snapshot.hasChildren()) {
                    snapshot.getChildren().iterator().forEachRemaining(new Consumer<DataSnapshot>() {
                        @Override
                        public void accept(DataSnapshot dataSnapshot) {
                            listCard.add(dataSnapshot.getValue(CartItem.class));
                        }
                    });
                    int total = 0;
                    for (CartItem item : listCard) {
                        total = Integer.parseInt(item.getPrice()) + total;
                    }
                    totalAmout.setText(total + "");
                    setAdapter();
                    adapterCart.notifyDataSetChanged();
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
        adapterCart = new AdapterCart(this, listCard);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapterCart);
    }

    @Override
    public void onClick(View view) {
        if (view == btnPlaceOrder) {
            for (CartItem cartItem : listCard) {
                Order order = new Order();
                order.setUserId(helper.getUser().getUid());
                order.setName(cartItem.getName());
                order.setPrice(cartItem.getPrice());
                order.setDescription(cartItem.getDiscription());
                order.setDeliverStatus(false);
                order.setProductId(cartItem.getProductId());
                order.setAcceptStatus(false);
                String key = orderRef.push().getKey();
                order.setOrderId(key);
                orderRef.child(key).setValue(order);
            }
            cartReference.child(helper.getUser().getUid()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    listCard.clear();
                    adapterCart.notifyDataSetChanged();
                    finish();
                    Toast.makeText(OrdersActivity.this, "Order place successfully", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (view == btnAddProduct) {
            startActivity(new Intent(this, ScanProduct.class));
        }
    }
}