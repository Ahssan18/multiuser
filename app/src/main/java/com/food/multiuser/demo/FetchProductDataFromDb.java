package com.food.multiuser.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.food.multiuser.Model.Product;
import com.food.multiuser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FetchProductDataFromDb extends AppCompatActivity {

    //declaration
    String productID;
    Product product;//global scope : we can save data in it, can update values in case of edit/update product

    DatabaseReference reference;

    ProgressDialog dialog;

    ArrayList<Product> productArrayList;

    TextView tvProduct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_product_data_from_db);
        setTitle("Get Product Details");

        //initialization
        productID = "-N8h7I1C645KHQg5zYGY";//when we scn barcode, product id returns
        product = new Product();

        productArrayList = new ArrayList<>();
        productArrayList.clear();

        reference = FirebaseDatabase.getInstance().getReference();

        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);

        tvProduct = findViewById(R.id.textViewProduct);


        //get single record
        getProductDetails(productID);
    }

    private void getProductDetails(String productID) {
        dialog.setTitle("loading product details");
        dialog.show();

        //when need to get single record with some id: use SingleValueEvent() method
        reference.child("Products").child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismiss();

                if (snapshot.exists()){
                    //fetch record
                    product = snapshot.getValue(Product.class);

                    Log.d("TAG", "onDataChange: Product Record: \n" + product.toString());

                    tvProduct.setText(product.toString());
                }else{
                    Toast.makeText(FetchProductDataFromDb.this, "No record found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        dialog.setTitle("Loading...");
//        dialog.show();
//
//        //if fetch all records from products
//        reference.child("Products").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                dialog.dismiss();
//
//                if (snapshot.exists()){
//                    //data found
////                    Log.d("TAG", "onDataChange: NoOfRecords: " + snapshot.getChildrenCount());
//
//                    //fetch all records
//                    for(DataSnapshot temp : snapshot.getChildren()){
//                        //get record
//                        Product tempProduct = new Product();
//                        tempProduct = temp.getValue(Product.class);
//
//                        //check product match with
//                        if (tempProduct.getMenuId().equals(productID)){
//                            Log.d("TAG", "onDataChange: Product matched: " + tempProduct.toString());
//                        }
//
//                        //populate arraylist object
//                        productArrayList.add(product);//add record to arraylist
//
//                    }
//                    //when all data fetched
//                    Log.d("TAG", "onDataChange: Products: " + productArrayList);
//
//                }else{
//                    //no record found
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}