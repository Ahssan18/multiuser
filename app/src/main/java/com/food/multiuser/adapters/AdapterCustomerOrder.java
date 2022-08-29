package com.food.multiuser.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Helper;
import com.food.multiuser.Model.Order;
import com.food.multiuser.R;
import com.food.multiuser.activity.FeedBackActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterCustomerOrder extends RecyclerView.Adapter<AdapterCustomerOrder.CustomOrder> {
    private List<Order> orderList;
    private Context context;
    private DatabaseReference orderRef;
    private Helper helper;


    public AdapterCustomerOrder(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
        orderRef = FirebaseDatabase.getInstance().getReference("order");
        helper = new Helper(context);

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
            holder.btnReceived.setClickable(false);
            holder.btnReceived.setText("Received");
            holder.btnReceived.setBackgroundColor(Color.GRAY);
        }
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

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class CustomOrder extends RecyclerView.ViewHolder {

        private TextView title, description, price, orderBy;
        private Button btnReceived;

        public CustomOrder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            orderBy = itemView.findViewById(R.id.tv_order_by);
            description = itemView.findViewById(R.id.tv_description);
            price = itemView.findViewById(R.id.tv_unit_price);
            btnReceived = itemView.findViewById(R.id.btnReceived);
        }
    }
}
