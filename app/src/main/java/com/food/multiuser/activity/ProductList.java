package com.food.multiuser.activity;

import static com.food.multiuser.Common.Common.PICK_IMAGE_REQUEST;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.food.multiuser.Common.Common;
import com.food.multiuser.Helper;
import com.food.multiuser.Interface.ItemClickListener;
import com.food.multiuser.Model.Product;
import com.food.multiuser.R;
import com.food.multiuser.ViewHolder.ProductViewHolder;
import com.food.multiuser.demo.AddProductWithQR;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProductList extends AppCompatActivity {
    RecyclerView recyclerView_product;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton floatingActionButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference productlist;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private Helper helper;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter;
    String categoryId = "";
    EditText editname, editDiscription, editPrice, editQuantity;
    Button scanbutton, btnSelect, btnUpload;
    ConstraintLayout productlayout;
    EditText editBarcode;
    public static TextView resultbarcode;
    Uri saveuri;
    Product newproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        helper = new Helper(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        productlist = firebaseDatabase.getReference("Products");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        recyclerView_product = findViewById(R.id.recyclerview_product);
        recyclerView_product.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView_product.setLayoutManager(layoutManager);
        productlayout = findViewById(R.id.productlayout);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        if (helper.getUser().getUsertype() == 1) {
            floatingActionButton.setVisibility(View.GONE);
        } else {
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductList.this, AddProductWithQR.class));
                finish();
            }
        });
        LoadProductList();
    }

    private void LoadProductList() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productlist
        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.product_name.setText(model.getName());
                Picasso.with(getBaseContext())
                        .load(model.getBarcode())
                        .into(viewHolder.product_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    Intent intent=new Intent(ProductList.this,ProductDetailActivity.class);
                    intent.putExtra("post",model);
                    startActivity(intent);
                    }

                });
            }
        };
        firebaseRecyclerAdapter.notifyDataSetChanged();  //Refresh data if data is changed
        recyclerView_product.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveuri = data.getData();
            btnSelect.setText("Image Selected!");
        }
    }

    //Update/Delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)) {
            DeleteProduct(firebaseRecyclerAdapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void DeleteProduct(String key) {
        productlist.child(key).removeValue();
        Toast.makeText(this, "Product Deleted!!!", Toast.LENGTH_SHORT).show();
    }
}
