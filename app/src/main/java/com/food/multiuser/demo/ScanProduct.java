package com.food.multiuser.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.food.multiuser.Model.CartItem;
import com.food.multiuser.Model.Product;
import com.food.multiuser.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class ScanProduct extends AppCompatActivity {

    Button btnScanQR;
    String PRODUCT_ID;
    ProgressDialog dialog;

    DatabaseReference reference;
    FirebaseAuth auth;
    TextView tvProduct;
    ConstraintLayout scanproductlayout;
    CartItem cartItem;
    private List<Product> productList;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);
        setTitle("Scan Product Code");
        cartItem = new CartItem();
        productList = new ArrayList<>();
        //initialization
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);

        PRODUCT_ID = "";
        product = new Product();

        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        tvProduct = findViewById(R.id.textViewProduct);
        scanproductlayout = findViewById(R.id.scanproductlayout);

        btnScanQR = findViewById(R.id.btnScanQR);

        //apply click listener
        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call method to scan QR Code
                scanQRCode();
            }
        });
    }

    private void scanQRCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a QR Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barcodeLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    PRODUCT_ID = result.getContents();
                    getProductDetails(PRODUCT_ID);

                } else {
                    Log.d("TAG", "BarcodeLauncher:: No Code Found ");
                }

            });

    private void getProductDetails(String productID) {
        dialog.setTitle("loading product details");
        dialog.show();


        //when need to get single record with some id: use SingleValueEvent() method
        reference.child("Products").child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismiss();

                if (snapshot.getKey() != null) {
                    //fetch record
                    product = snapshot.getValue(Product.class);

                    Log.d("TAG", "onDataChange: Product Record: \n" + product.toString());

                    tvProduct.setText(product.toString());

                    //add product to cart after scan
                    addProductToCart(product);

                } else {
                    Toast.makeText(ScanProduct.this, "No record found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addProductToCart(Product product) {
        dialog.setTitle("Adding to Cart...");
        dialog.show();
        int price = 0;
        boolean found = false;
        Product existingProduct = null;
        for (int i = 0; i < productList.size(); i++) {
            Product product1 = productList.get(i);
            if (product1.getProductId().equals(product.getProductId())) {
                found = true;
                existingProduct = product1;
            }
        }
        if (found) {
            for (int i = 0; i < productList.size(); i++) {
                Product product1 = productList.get(i);
                if (product1.getProductId().equals(product.getProductId())) {
                    int quanity = existingProduct.getQuantity() + 1;
                    product1.setQuantity(quanity);
                    productList.set(i, product1);
                }
            }
        } else {
            productList.add(product);
        }
        for (int i = 0; i < productList.size(); i++) {
            Product product1 = productList.get(i);
            price = price + (product1.getPrice() * product1.getQuantity());
        }
        cartItem.setTotalPrice(price + "");
        cartItem.setList(productList);
        reference.child("MyCart").child(auth.getCurrentUser().getUid())
                .setValue(cartItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(ScanProduct.this, "Product added to your cart!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(ScanProduct.this, "Product Not Added to Cart", Toast.LENGTH_SHORT).show();
                    }
                });
        ;
    }
}