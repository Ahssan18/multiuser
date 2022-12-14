package com.food.multiuser.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Model.FeedBack;
import com.food.multiuser.Model.Product;
import com.food.multiuser.R;
import com.food.multiuser.adapters.AdapterFeedback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView title, description, tvRating;
    private RecyclerView recycleFeedback;
    private Product product;
    private DatabaseReference feedbackRef;
    private List<FeedBack> feedBackList;
    private ImageView productQr, iv_qr;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initObjects();
        clickListener();
        getProdectFeedback();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more_tab_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //download product qr then use this qr for placing order
        if (item.getItemId() == R.id.download_qr) {
            try {
                SaveImage(((BitmapDrawable) iv_qr.getDrawable()).getBitmap());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void SaveImage(Bitmap finalBitmap) {
        File myDir;
        String path = null;
        OutputStream out = null;
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            ContentResolver resolver = this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image-" + product.getName() + "-" + product.getProductId());
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "Almadina_Bakers");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                path = imageUri.getPath();
                out = resolver.openOutputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            myDir = new File(root + "/Almadina_Bakers");
            myDir.mkdirs();
            String fname = "Image-" + product.getName() + "-" + product.getProductId() + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists())
                file.delete();
            try {
                path = file.getPath();
                out = new FileOutputStream(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try {
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(this,
                    new String[]{path}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ProductDetailActivity.this, "Product Qr is saved successfully!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProdectFeedback() {
        //get all feedback against some product
        feedbackRef.child(product.getProductId()).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        snapshot.getChildren().iterator().forEachRemaining(new Consumer<DataSnapshot>() {
                            @Override
                            public void accept(DataSnapshot dataSnapshot) {
                                feedBackList.add(dataSnapshot.getValue(FeedBack.class));
                            }
                        });
                        float averageRating = 0;
                        if (feedBackList != null && feedBackList.size() > 0) {
                            for (FeedBack feedBack : feedBackList) {
                                averageRating = averageRating + feedBack.getRating();
                            }
                            averageRating = averageRating / feedBackList.size();
                            tvRating.setText(averageRating + "/" + 5);
                        }
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setAdapter() {
        AdapterFeedback adapterFeedback = new AdapterFeedback(feedBackList);
        recycleFeedback.setLayoutManager(new LinearLayoutManager(this));
        recycleFeedback.setAdapter(adapterFeedback);
        adapterFeedback.notifyDataSetChanged();
    }

    private void clickListener() {
    }

    private void initObjects() {
        feedBackList = new ArrayList<>();
        feedbackRef = FirebaseDatabase.getInstance().getReference("feedback");
        product = getIntent().getParcelableExtra("post");
        title = findViewById(R.id.tv_title);
        tvRating = findViewById(R.id.tv_rating);
        productQr = findViewById(R.id.product_image);
        iv_qr = findViewById(R.id.iv_qr);
        description = findViewById(R.id.tv_description);
        recycleFeedback = findViewById(R.id.recycle_feedback);
        if (product != null) {
            title.setText(product.getName());
            description.setText(product.getDiscription());
            Picasso.with(getBaseContext())
                    .load(product.getProductImage())
                    .into(productQr);
        }
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(product.getProductId(),
                    BarcodeFormat.QR_CODE, 256, 256);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
            iv_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }
}