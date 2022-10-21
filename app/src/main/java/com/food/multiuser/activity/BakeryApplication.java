package com.food.multiuser.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.food.multiuser.Model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BakeryApplication extends Application {
    DatabaseReference productRef;
    private FirebaseDatabase firebaseDatabase;
    public static List<Product> productsList;

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseDatabase = FirebaseDatabase.getInstance();
        productRef = firebaseDatabase.getReference("Products");
        productsList = new ArrayList<>();
        // get all products from filrebase and listen when a new product is added oe edited some one
        productRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()) {
                    try {
                        Product product = snapshot.getValue(Product.class);
                        productsList.add(product);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()) {
                    try {
                        Product product = snapshot.getValue(Product.class);
                        for (int i = 0; i < productsList.size(); i++)
                            if (product.getProductId().equals(productsList.get(i).getProductId())) {
                                productsList.set(i, product);
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int position = -1;
                if (snapshot.exists()) {
                    try {
                        Product product = snapshot.getValue(Product.class);
                        for (int i = 0; i < productsList.size(); i++) {
                            if (product.getProductId().equals(productsList.get(i).getProductId())) {
                                position = i;
                                break;
                            }
                        }
                        if (position != -1)
                            productsList.remove(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
