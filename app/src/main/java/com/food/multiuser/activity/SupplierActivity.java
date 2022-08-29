package com.food.multiuser.activity;

import static com.food.multiuser.Common.Common.PICK_IMAGE_REQUEST;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Common.Common;
import com.food.multiuser.Interface.ItemClickListener;
import com.food.multiuser.Model.Supplier;
import com.food.multiuser.R;
import com.food.multiuser.ViewHolder.SupplierViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class SupplierActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    //Firebase
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference Supplier;
    FirebaseRecyclerAdapter<Supplier, SupplierViewHolder> firebaseRecyclerAdapter;
    //view
    RecyclerView recyclerView_menu;
    RecyclerView.LayoutManager layoutManager;
    //Add new menu layout
    EditText editname, editemail, editphonenumber;
    Button btnSelect, btnUpload;
    Supplier newsupplier;
    ConstraintLayout supplierlayout;

    Uri saveuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        //init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        Supplier = firebaseDatabase.getReference("Supplier");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener((view) -> {
            ShowDialog();
        });
        //init recyclerview
        recyclerView_menu = (RecyclerView) findViewById(R.id.recyclerview_menu);
        recyclerView_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_menu.setLayoutManager(layoutManager);
        supplierlayout = (ConstraintLayout)findViewById(R.id.supplierlayout);

        LoadMenu();
    }
    private void ShowDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SupplierActivity.this);
        alertDialog.setTitle("Add New Supplier");
        alertDialog.setMessage("Please fill full Information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_supplier_layout, null);

        editname = add_menu_layout.findViewById(R.id.supplier_name);
        editemail = add_menu_layout.findViewById(R.id.supplier_email);
        editphonenumber = add_menu_layout.findViewById(R.id.supplier_phonenumber);

        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        //Event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage(); //Let you choose image for gallary and save url of this image in firebase
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });
        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_person_login);
        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //here, just create new category
                if(newsupplier != null) {
                    Supplier.push().setValue(newsupplier);
                    Snackbar.make(supplierlayout, "New Supplier " + newsupplier.getName()+ " was Added", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    private void UploadImage() {
        if (saveuri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            String imagename = UUID.randomUUID().toString();
            StorageReference imagefolder = storageReference.child("images/" + imagename);
            imagefolder.putFile(saveuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(SupplierActivity.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                            imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new Staff if image uploaded and we can get download link
                                    newsupplier = new Supplier();
                                    newsupplier.setName(editname.getText().toString());
                                    newsupplier.setEmail(editemail.getText().toString());
                                    newsupplier.setPhoneNumber(editphonenumber.getText().toString());
                                    newsupplier.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(SupplierActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot tasksnapshot) {
                            // don't worry about this error
                            double progress = (100 * tasksnapshot.getBytesTransferred() / tasksnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + progress + "%");
                        }
                    });
        }
    }
    //press CtrL+O
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            saveuri = data.getData();
            btnSelect.setText("Image Selected!");
        }
    }
    private void ChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void LoadMenu() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Supplier, SupplierViewHolder>(
                Supplier.class,
                R.layout.supplier_item,
                SupplierViewHolder.class,
                Supplier
        ) {
            @Override
            protected void populateViewHolder(SupplierViewHolder viewHolder, Supplier model, int i) {
                viewHolder.supplier_name.setText(model.getName());
                viewHolder.supplier_email.setText(model.getEmail());
                viewHolder.supplier_phonenumber.setText(model.getPhoneNumber());
                Picasso.with(SupplierActivity.this).load(model.getImage())
                        .into(viewHolder.supplier_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //send Category Id and start new activity
                    }
                });
            }
        };
        firebaseRecyclerAdapter.notifyDataSetChanged();  //Refresh data if data is changed
        recyclerView_menu.setAdapter(firebaseRecyclerAdapter);
    }
    //Update/Delete
    //press Ctrl+O
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            ShowUpdateDialog(firebaseRecyclerAdapter.getRef(item.getOrder()).getKey(), firebaseRecyclerAdapter.getItem(item.getOrder()));
        } else if(item.getTitle().equals(Common.DELETE)){
            DeleteCategory(firebaseRecyclerAdapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }
    private void ShowUpdateDialog(final String key, final Supplier item) {
        //Just copy code from showdialog and modify
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SupplierActivity.this);
        alertDialog.setTitle("Update Supplier");
        alertDialog.setMessage("Please fill full Information");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_supplier_layout, null);

        editname = add_menu_layout.findViewById(R.id.supplier_name);
        editemail = add_menu_layout.findViewById(R.id.supplier_email);
        editphonenumber = add_menu_layout.findViewById(R.id.supplier_phonenumber);

        //Set default values for view
        editname.setText(item.getName());
        editemail.setText(item.getEmail());
        editphonenumber.setText(item.getPhoneNumber());

        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);
        //Event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage(); //Let you choose image for gallary and save url of this image in firebase
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeImage(item);
            }
        });
        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_person_login);
        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //Update information
                item.setName(editname.getText().toString());
                item.setEmail(editemail.getText().toString());
                item.setPhoneNumber(editphonenumber.getText().toString());

                Supplier.child(key).setValue(item);
                Snackbar.make(supplierlayout, "Supplier " + item.getName()+ " was Edited", Snackbar.LENGTH_LONG).show();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    private void ChangeImage(final Supplier item) {
        //Copy code from UploadImage and modify
        if (saveuri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            String imagename = UUID.randomUUID().toString();
            StorageReference imagefolder = storageReference.child("images/" + imagename);
            imagefolder.putFile(saveuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(SupplierActivity.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                            imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new category if image uploaded and we can get download link
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(SupplierActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot tasksnapshot) {
                            // don't worry about this error
                            double progress = (100 * tasksnapshot.getBytesTransferred() / tasksnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + progress + "%");
                        }
                    });
        }
    }
    private void DeleteCategory(String key) {
        Supplier.child(key).removeValue();
        Toast.makeText(this, "Supplier Deleted!!!", Toast.LENGTH_SHORT).show();
    }
}
