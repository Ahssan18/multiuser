package com.food.multiuser.activity;

import static com.food.multiuser.activity.BakeryApplication.productsList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceOrdersActivity extends AppCompatActivity implements View.OnClickListener {

    static RecyclerView recyclerview;
    LinearLayoutManager layoutManager;
    private DatabaseReference reference, orderRef, cartReference;
    private List<Product> listCard;
    private AdapterPlaceOrder AdapterPlaceOrder;
    private FirebaseAuth auth;
    private String TAG = "PlaceOrdersActivity";
    private Button btnPlaceOrder, btnAddProduct;
    private Helper helper;
    private TextView totalAmout;
    private CartItem cartItem;
    private int position;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_meni, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cart: {
                clearCart();
                totalAmout.setText("");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearCart() {
        //clear card if a user want to clear data from shopping cart
        cartReference.child(helper.getUser().getUid()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listCard.clear();
                if (AdapterPlaceOrder != null)
                    AdapterPlaceOrder.notifyDataSetChanged();
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
        recyclerview.setLayoutManager(layoutManager);

    }

    private void setAdapter() {
        AdapterPlaceOrder = new AdapterPlaceOrder(this, listCard);
        recyclerview.setAdapter(AdapterPlaceOrder);
    }

    @Override
    public void onClick(View view) {
        if (view == btnPlaceOrder) {
            if (cartItem != null) {
                //after adding products to shopping cart and place order using place order button
                //and when order place successfully clear data from cart
                Order order = new Order();
                order.setUserId(helper.getUser().getUid());
                order.setTotalPrice(cartItem.getTotalPrice() + "");
                order.setDeliverStatus(false);
                order.setAcceptStatus(false);
                order.setProductList(listCard);
                order.setTimeStamp(System.currentTimeMillis());
                String key = orderRef.push().getKey();
                order.setOrderId(key);
                orderRef.child(key).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cartReference.child(helper.getUser().getUid()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                position = 0;
                                updateProductsQuantity(position);
                            }

                            @SuppressLint("NotifyDataSetChanged")
                            private void updateProductsQuantity(int position) {
                                Log.e(TAG, "productList " + productsList.size());
                                if (position < listCard.size()) {
                                    boolean found = false;
                                    Product product = listCard.get(position);
                                    int remainingQuantity = -1;
                                    for (Product actualProduct : productsList) {
                                        if (product.getProductId().equals(actualProduct.getProductId())) {
                                            remainingQuantity = actualProduct.getQuantity() - product.getOrderQuantity();
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (found && remainingQuantity != -1) {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("quantity", remainingQuantity);
                                        reference.child("Products").child(product.getProductId()).updateChildren(map);
                                        found = false;
                                    }
                                    position = position + 1;
                                    updateProductsQuantity(position);
                                } else {
                                    Toast.makeText(PlaceOrdersActivity.this, "Order place successfully", Toast.LENGTH_SHORT).show();
                                    listCard.clear();
                                    AdapterPlaceOrder.notifyDataSetChanged();
                                    finish();
                                }
                            }
                        });
                    }
                });
            }


        } else if (view == btnAddProduct) {
            startActivity(new Intent(this, ScanProduct.class));
            finish();
        }
    }
}