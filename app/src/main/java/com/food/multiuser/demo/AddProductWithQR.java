package com.food.multiuser.demo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.food.multiuser.Model.Product;
import com.food.multiuser.R;
import com.food.multiuser.activity.ProductListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AddProductWithQR extends AppCompatActivity {

    Button btnGenerate, btnSaveProduct;
    ImageView imageViewProduct, iv_picker;
    EditText editTitle, editDesc, editPrice, editQty;
    DatabaseReference reference, productReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String strProductKey;
    ProgressDialog dialog;
    Bitmap bitmap;
    ByteArrayOutputStream baos;
    private Product product;
    byte[] data;
    boolean editImage = false, editProduct = false;
    public String APP_TAG = "MyCustomApp";
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private final String photoFileName = "photo.jpg";
    private File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_with_qr);
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
        imageViewProduct = findViewById(R.id.imageViewQR);
        iv_picker = findViewById(R.id.iv_picker);
        btnSaveProduct = findViewById(R.id.buttonSave);
        if (getIntent().hasExtra("product")) {
            product = getIntent().getParcelableExtra("product");
            editProduct = true;
            setTitle("Edit Product");
            editTitle.setText(product.getName());
            editDesc.setText(product.getDiscription());
            editPrice.setText(String.valueOf(product.getPrice()));
            editQty.setText(String.valueOf(product.getQuantity()));
            Picasso.with(getBaseContext())
                    .load(product.getProductImage())
                    .into(imageViewProduct);
            btnSaveProduct.setText("Update Product");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        bitmap = ((BitmapDrawable) imageViewProduct.getDrawable()).getBitmap();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);
            editImage = false;
        } else {
            setTitle("Add New Product");
        }
        iv_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picImage();
            }
        });
        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    if (!editTitle.getText().toString().isEmpty()) {
                        if (!editDesc.getText().toString().isEmpty()) {
                            if (!editPrice.getText().toString().isEmpty()) {
                                if (!editQty.getText().toString().isEmpty()) {
                                    if (product != null && product.getProductId() != null) {
                                        strProductKey = product.getProductId();
                                    } else {
                                        strProductKey = reference.push().getKey();
                                    }
                                    String title = editTitle.getText().toString();
                                    String description = editDesc.getText().toString();
                                    int qty = Integer.parseInt(editQty.getText().toString());
                                    int price = Integer.parseInt(editPrice.getText().toString());
                                    Product product = new Product();
                                    product.setProductId(strProductKey);
                                    product.setName(title);
                                    product.setQuantity(qty);
                                    product.setDiscription(description);
                                    product.setPrice(price);
                                    getImageFileFromImageView(product);
                                } else {
                                    Toast.makeText(AddProductWithQR.this, "Please add product quantity !", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddProductWithQR.this, "Please enter product price !", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProductWithQR.this, "Please add product description !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddProductWithQR.this, "Please add product title !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddProductWithQR.this, "Please select product image!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void picImage() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.image_picker, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = getPhotoFileUri(photoFileName);
                if (photoFile != null) {
                    Uri fileProvider =
                            FileProvider.getUriForFile(AddProductWithQR.this, "com.food.multiuser", photoFile);
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                    startActivityForResult(takePicture, 0);
                    deleteDialog.dismiss();
                }
            }
        });
        deleteDialogView.findViewById(R.id.tv_galary).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                        deleteDialog.dismiss();
                    }
                });

        deleteDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    imageViewProduct.setImageBitmap(bitmap);
                    bitmap = ((BitmapDrawable) imageViewProduct.getDrawable()).getBitmap();
                    if (product != null) {
                        editImage = true;
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    Uri selectedImage = data.getData();
                    imageViewProduct.setImageURI(selectedImage);
                    bitmap = ((BitmapDrawable) imageViewProduct.getDrawable()).getBitmap();
                    if (product != null) {
                        editImage = true;
                    }
                }
                break;
        }
    }

    private void getImageFileFromImageView(Product product1) {
        if (editProduct) {
            if (editImage) {
                UploadImageToStorage(product1);
            } else {
                product1.setProductImage(product.getProductImage());
                saveProduct(product1);
            }
        } else {
            UploadImageToStorage(product1);
        }

    }

    private void UploadImageToStorage(Product product1) {
        imageViewProduct.setDrawingCacheEnabled(true);
        imageViewProduct.buildDrawingCache();
        baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();
        uploadImage(data, product1);
    }

    private void uploadImage(byte[] data, Product prod) {
        if (!editProduct) {
            dialog.setTitle("Adding product...");
        } else {
            dialog.setTitle("Updating product...");
        }
        dialog.show();
        final StorageReference fileRef =
                storageReference.child("barcodes/").child(prod.getProductId() + ".jpeg");
        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        prod.setProductImage(uri.toString());
                        saveProduct(prod);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                dialog.dismiss();
                Toast.makeText(AddProductWithQR.this, "Failed to upload product Image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProduct(Product prod) {
        reference.child("Products").child(prod.getProductId()).setValue(prod)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        if (product != null) {
                            Toast.makeText(AddProductWithQR.this, "Product edit successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddProductWithQR.this, "Product Added", Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(AddProductWithQR.this, ProductListActivity.class));
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