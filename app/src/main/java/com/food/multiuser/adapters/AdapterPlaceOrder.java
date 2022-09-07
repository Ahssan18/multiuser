package com.food.multiuser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Model.Product;
import com.food.multiuser.R;

import java.util.List;

public class AdapterPlaceOrder extends RecyclerView.Adapter<AdapterPlaceOrder.CartView> {
    private Context context;
    private List<Product> productList;
    private boolean acceptStatus;

    public AdapterPlaceOrder(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.acceptStatus = acceptStatus;
    }

    @NonNull
    @Override
    public CartView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_order_cart_layout, parent, false);
        return new CartView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartView holder, int position) {
        setData(productList.get(position), holder);
    }

    private void setData(Product cartItem, CartView holder) {
        holder.title.setText(cartItem.getName());
        holder.description.setText(cartItem.getDiscription());
        holder.perUnitPrice.setText("Unit Price :" + cartItem.getPrice());
        holder.quantity.setText(cartItem.getQuantity() + "");

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class CartView extends RecyclerView.ViewHolder {

        private TextView title, description, perUnitPrice, quantity;

        public CartView(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            quantity = itemView.findViewById(R.id.tv_unit_quantity);
            description = itemView.findViewById(R.id.tv_description);
            perUnitPrice = itemView.findViewById(R.id.tv_unit_price);
        }
    }
}
