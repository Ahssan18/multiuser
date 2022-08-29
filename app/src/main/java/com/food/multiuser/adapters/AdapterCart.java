package com.food.multiuser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Model.CartItem;
import com.food.multiuser.R;

import java.util.List;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartView> {
    private Context context;
    private List<CartItem> cartItems;

    public AdapterCart(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_layout, parent, false);
        return new CartView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartView holder, int position) {
        setData(cartItems.get(position), holder);
    }

    private void setData(CartItem cartItem, CartView holder) {
        holder.title.setText(cartItem.getName());
        holder.description.setText(cartItem.getDiscription());
        holder.perUnitPrice.setText("Unit Price :" + cartItem.getPrice());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartView extends RecyclerView.ViewHolder {

        private TextView title, description, perUnitPrice;

        public CartView(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_description);
            perUnitPrice = itemView.findViewById(R.id.tv_unit_price);
        }
    }
}
