package com.food.multiuser.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Common.Common;
import com.food.multiuser.Interface.ItemClickListener;
import com.food.multiuser.R;

public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {
    public TextView staff_name;
    public TextView staff_email;
    public TextView staff_phonenumber;
    public ImageView staff_image;

    private ItemClickListener itemClickListener;
    public StaffViewHolder(View itemView){
        super(itemView);

        staff_name = (TextView) itemView.findViewById(R.id.staff_name);
        staff_email = (TextView) itemView.findViewById(R.id.staff_email);
        staff_phonenumber = (TextView) itemView.findViewById(R.id.staff_phonenumber);
        staff_image = (ImageView) itemView.findViewById(R.id.staff_image);

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
