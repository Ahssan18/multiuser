package com.food.multiuser.activity;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.food.multiuser.Helper;
import com.food.multiuser.Model.Order;
import com.food.multiuser.R;
import com.food.multiuser.adapters.AdapterCustomerOrder;
import com.food.multiuser.adapters.CustomerViewPagerAdapter;
import com.food.multiuser.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CustomerOrderActivity extends AppCompatActivity {



    private TabLayout tabLayout;
    private ViewPager viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        initObjects();
    }

    private void initObjects() {
        tabLayout = findViewById(R.id.toolbar);
        viewPager2 = findViewById(R.id.viewPager);
        getSupportActionBar().setTitle("Order");
        CustomerViewPagerAdapter viewPagerAdapter = new CustomerViewPagerAdapter(getSupportFragmentManager());
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager2);

    }



}