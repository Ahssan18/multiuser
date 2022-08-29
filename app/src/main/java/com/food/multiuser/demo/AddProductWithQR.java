package com.food.multiuser.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.food.multiuser.Model.Product;
import com.food.multiuser.activity.ProductList;
import com.food.multiuser.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class AddProductWithQR extends AppCompatActivity {

    Button btnGenerate, btnSaveProduct;
    ImageView imageViewQR;
    EditText editTitle, editDesc, editPrice, editQty;

    DatabaseReference reference, productReference;

    FirebaseStorage storage;
    StorageReference storageReference;

    private String strProductKey;

    ProgressDialog dialog;

    //upload QR Code
    Bitmap bitmap;
    ByteArrayOutputStream baos;
    byte [] data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_with_qr);
        setTitle("Add New Product");

        //initialization
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);

        reference = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        baos = new ByteArrayOutputStream();

        editDesc = findViewById(R.id.editDescription);
        editTitle = findViewById(R.id.editProduct);
        editQty = findViewById(R.id.editQty);
        editPrice = findViewById(R.id.editPrice);

        imageViewQR = findViewById(R.id.imageViewQR);

        btnGenerate = findViewById(R.id.buttonGenerate);
        btnSaveProduct = findViewById(R.id.buttonSave);
        btnSaveProduct.setEnabled(false);//disable button until qr generated

        //apply click listener
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get product push key
                strProductKey = reference.push().getKey();

                //generate qr

                //initialize multi format writer
                MultiFormatWriter writer = new MultiFormatWriter();
                try{
                    //initialize QR Matrix
                    BitMatrix matrix = writer.encode(strProductKey,
                            BarcodeFormat.QR_CODE, 256, 256);
                    BarcodeEncoder encoder  = new BarcodeEncoder();

                    //initialize QR Image
                    bitmap = encoder.createBitmap(matrix);

                    //replace generate button text
                    btnGenerate.setText("Re-Generate QR");

                    //set QR Image to ImageView
                    imageViewQR.setImageBitmap(bitmap);


                    //when qr generated: button enabled
                    btnSaveProduct.setEnabled(true);

                }catch (Exception ex){
                    ex.getMessage();

                }


            }
        });
        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //read values
                String title = editTitle.getText().toString();
                String description = editDesc.getText().toString();
                String qty = editQty.getText().toString();
                String price = editPrice.getText().toString();

//                set values to
                Product product = new Product();

                product.setProductId(strProductKey);
                product.setName(title);
                product.setQuantity(qty);
                product.setDiscription(description);
                product.setPrice(price);

                //get image and upload
                getImageFileFromImageView(product);
            }
        });

    }

    private void getImageFileFromImageView(Product product) {


        // Get the data from an ImageView as bytes
        imageViewQR.setDrawingCacheEnabled(true);
        imageViewQR.buildDrawingCache();

        baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();

        //upload QR Image to firebase storage
        uploadImage(data, product);

    }

    private void uploadImage(byte[] data, Product prod ) {
        dialog.setTitle("Uploading QR...");
        dialog.show();

        final StorageReference fileRef =
                storageReference.child("barcodes/").child( prod.getProductId() +".jpeg");

        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Uri object has Download URL of uploaded file
                        //get save image's download URL.
                        prod.setBarcode(uri.toString());

                        //save QR Code's URL to customer profile
                        saveProduct(prod);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                // Handle unsuccessful uploads
                dialog.dismiss();
                Toast.makeText(AddProductWithQR.this, "QR Code Not Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProduct(Product prod) {
        reference.child("Products").child(prod.getProductId()).setValue(prod)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();

                        Toast.makeText(AddProductWithQR.this, "Product Added", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(AddProductWithQR.this, ProductList.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(AddProductWithQR.this, "Product Not Added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}