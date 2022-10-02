package com.food.multiuser.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Helper;
import com.food.multiuser.Model.Order;
import com.food.multiuser.R;
import com.food.multiuser.adapters.AdapterOrders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class OrderHistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Order> orderList;
    private DatabaseReference orderReference;
    private AdapterOrders adapterOrders;
    private View view;
    private Helper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_history, container, false);
        initObjects();
        setData();
        return view;
    }

    private void initObjects() {
        helper = new Helper(getActivity());
        orderList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycleOrders);
        orderReference = FirebaseDatabase.getInstance().getReference("order");
    }

    private void setData() {
        Query query = orderReference.orderByChild("acceptStatus").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    snapshot.getChildren().iterator().forEachRemaining(new Consumer<DataSnapshot>() {
                        @Override
                        public void accept(DataSnapshot dataSnapshot) {
                            orderList.add(dataSnapshot.getValue(Order.class));
                        }
                    });
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setAdapter() {
        Collections.reverse(orderList);
        adapterOrders = new AdapterOrders(orderList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterOrders);
        adapterOrders.notifyDataSetChanged();
    }

}