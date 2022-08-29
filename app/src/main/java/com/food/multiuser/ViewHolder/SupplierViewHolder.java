package com.food.multiuser.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Common.Common;
import com.food.multiuser.Interface.ItemClickListener;
import com.food.multiuser.R;

public class SupplierViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {
    public TextView supplier_name;
    public TextView supplier_email;
    public TextView supplier_phonenumber;
    public ImageView supplier_image;

    private ItemClickListener itemClickListener;
    public SupplierViewHolder(View itemView){
        super(itemView);

        supplier_name = (TextView)itemView.findViewById(R.id.supplier_name);
        supplier_email = (TextView) itemView.findViewById(R.id.supplier_email);
        supplier_phonenumber = (TextView) itemView.findViewById(R.id.supplier_phonenumber);
        supplier_image = (ImageView) itemView.findViewById(R.id.supplier_image);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the Action");
        contextMenu.add(0,0, getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1, getAdapterPosition(), Common.DELETE);
    }
}
