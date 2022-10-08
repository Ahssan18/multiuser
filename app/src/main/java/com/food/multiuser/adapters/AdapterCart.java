package com.food.multiuser.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Helper;
import com.food.multiuser.Model.Order;
import com.food.multiuser.Model.Product;
import com.food.multiuser.R;
import com.food.multiuser.activity.FeedBackActivity;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartView> {
    private Context context;
    private Order order;
    private boolean acceptStatus;
    private Helper helper;
    int userType = 1;

    public AdapterCart(Context context, Order order) {
        this.context = context;
        this.order = order;
        this.acceptStatus = order.isAcceptStatus();
        helper = new Helper(context);
        userType = helper.getUser().getUsertype();
    }

    @NonNull
    @Override
    public CartView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_layout, parent, false);
        return new CartView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartView holder, int position) {
        setData(order.getProductList().get(position), holder, position, order);
    }

    private void setData(Product cartItem, CartView holder, int position, Order order) {
        if (acceptStatus && !cartItem.isFeedback() && userType == 1) {
            holder.btnFeedback.setVisibility(View.VISIBLE);
        } else {
            holder.btnFeedback.setVisibility(View.GONE);
        }
        holder.title.setText("Product Title : " + cartItem.getName());
        holder.description.setText("Product Details : " + cartItem.getDiscription());
        holder.perUnitPrice.setText("Unit Price : " + cartItem.getPrice());
        holder.quantity.setText("Product Quantity : " + cartItem.getOrderQuantity() + "");
        if (order.getTimeStamp() != -1) {
            holder.tv_orderData.setText(helper.getDate(order.getTimeStamp()));
        }

        holder.btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FeedBackActivity.class);
                intent.putExtra("productId", cartItem.getProductId());
                intent.putExtra("name", cartItem.getName());
                intent.putExtra("productkey", String.valueOf(position));
                intent.putExtra("orderId", order.getOrderId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return order.getProductList().size();
    }

    public class CartView extends RecyclerView.ViewHolder {

        private TextView title, description, perUnitPrice, quantity, tv_orderData;
        private Button btnFeedback;

        public CartView(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            tv_orderData = itemView.findViewById(R.id.tv_order_date);
            quantity = itemView.findViewById(R.id.tv_unit_quantity);
            description = itemView.findViewById(R.id.tv_description);
            perUnitPrice = itemView.findViewById(R.id.tv_unit_price);
            btnFeedback = itemView.findViewById(R.id.btn_feedback);
        }
    }
}
