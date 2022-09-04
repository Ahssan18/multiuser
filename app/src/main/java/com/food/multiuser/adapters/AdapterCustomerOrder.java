package com.food.multiuser.adapters;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Helper;
import com.food.multiuser.Model.Order;
import com.food.multiuser.R;
import com.food.multiuser.activity.FeedBackActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AdapterCustomerOrder extends RecyclerView.Adapter<AdapterCustomerOrder.CustomOrder> {
    private List<Order> orderList;
    private Context context;
    private DatabaseReference orderRef;
    private Helper helper;
    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;


    public AdapterCustomerOrder(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
        orderRef = FirebaseDatabase.getInstance().getReference("order");
        helper = new Helper(context);
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

    }

    @NonNull
    @Override
    public AdapterCustomerOrder.CustomOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_customer_order, parent, false);
        return new AdapterCustomerOrder.CustomOrder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCustomerOrder.CustomOrder holder, int position) {
        setData(orderList.get(position), holder);
    }

    private void setData(Order order, AdapterCustomerOrder.CustomOrder holder) {
        holder.title.setText(order.getName());
        holder.description.setText(order.getDescription());
        holder.price.setText(order.getPrice());
        holder.orderBy.setText("Order by : " + helper.getUser().getName());
        if (order.isAcceptStatus()) {
            holder.btnReceived.setClickable(true);
            holder.btnReceived.setVisibility(View.VISIBLE);
        }
        if (order.isDeliverStatus()) {
            holder.btn_cancel.setVisibility(View.GONE);
            holder.btnReceived.setClickable(false);
            holder.btnReceived.setText("Received");
            holder.ivReceipt.setVisibility(View.VISIBLE);
            holder.btnReceived.setBackgroundColor(Color.GRAY);
        }
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderRef.child(order.getOrderId()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Order cancel successfully!", Toast.LENGTH_SHORT).show();
                        orderList.remove(order);
                        notifyDataSetChanged();
                    }
                });
            }
        });
        holder.ivReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    generatePDF(order);
                } else {
                    requestPermission();
                }
            }
        });

        holder.btnReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderRef.child(order.getOrderId()).child("deliverStatus")
                        .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.btnReceived.setClickable(false);
                                holder.btnReceived.setText("Received");
                                notifyDataSetChanged();
                                holder.btnReceived.setBackgroundColor(Color.GRAY);
                                context.startActivity(new Intent(context, FeedBackActivity.class).putExtra("productId", order.getProductId()).putExtra("name", order.getName()));

                            }
                        });
            }
        });
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions((Activity) context, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
    }

    private void generatePDF(Order order) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();
        Paint otherData = new Paint();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(scaledbmp, 56, 40, paint);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(40);
        otherData.setTextSize(40);
        otherData.setTextAlign(Paint.Align.RIGHT);

        canvas.drawText(context.getString(R.string.app_name), 209, 120, title);
        canvas.drawText("Customer Name : " + helper.getUser().getName(), 10, 250, title);
        canvas.drawText("-----------------------------------------------------------", 10, 280, title);
        canvas.drawText("Product Name", 10, 330, title);
        canvas.drawText(order.getName(), 600, 330, title);
        canvas.drawText("Product Price", 10, 380, title);
        canvas.drawText(order.getPrice() + " RS", 600, 380, title);
        canvas.drawText("Product Quantity", 10, 430, title);
        canvas.drawText("1 unit", 600, 430, title);

        canvas.drawText("-----------------------------------------------------------", 10, 1000, title);
        canvas.drawText("Total Amount", 10, 1050, title);
        canvas.drawText(order.getPrice() + " RS", 600, 1050, title);
        pdfDocument.finishPage(myPage);
        File file = new File(Environment.getExternalStorageDirectory(), order.getOrderId() + ".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "Receipt generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class CustomOrder extends RecyclerView.ViewHolder {

        private TextView title, description, price, orderBy;
        private Button btnReceived, btn_cancel;
        private ImageView ivReceipt;

        public CustomOrder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            orderBy = itemView.findViewById(R.id.tv_order_by);
            description = itemView.findViewById(R.id.tv_description);
            price = itemView.findViewById(R.id.tv_unit_price);
            btnReceived = itemView.findViewById(R.id.btnReceived);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            ivReceipt = itemView.findViewById(R.id.iv_receipt);
        }
    }
}
