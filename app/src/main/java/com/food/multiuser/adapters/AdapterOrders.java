package com.food.multiuser.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Model.Order;
import com.food.multiuser.Model.User;
import com.food.multiuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.CustomOrder> {
    private List<Order> orderList;
    private Context context;
    private DatabaseReference orderRef, userRef;
    private String TAG="AdapterOrders";

    public AdapterOrders(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
        orderRef = FirebaseDatabase.getInstance().getReference("order");
        userRef = FirebaseDatabase.getInstance().getReference("Users");

    }

    @NonNull
    @Override
    public CustomOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_order, parent, false);
        return new CustomOrder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomOrder holder, int position) {
        setData(orderList.get(position), holder);
    }

    private void setData(Order order, CustomOrder holder) {
        holder.title.setText(order.getName());
        holder.description.setText(order.getDescription());
        holder.price.setText(order.getPrice());
        if (order.isAcceptStatus()) {
            holder.btnAccept.setClickable(false);
            holder.btnAccept.setBackgroundColor(Color.GRAY);
        }
        Log.e(TAG,"userId "+order.getUserId());
        userRef.child(order.getUserId()).child("UserDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                holder.orderBy.setText("Order by : " + user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderRef.child(order.getOrderId()).child("acceptStatus")
                        .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.btnAccept.setClickable(false);
                                notifyDataSetChanged();
                                holder.btnAccept.setBackgroundColor(Color.GRAY);

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
        private Button btnAccept;

        public CustomOrder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            orderBy = itemView.findViewById(R.id.tv_order_by);
            description = itemView.findViewById(R.id.tv_description);
            price = itemView.findViewById(R.id.tv_unit_price);
            btnAccept = itemView.findViewById(R.id.btnAccept);
        }
    }
}
