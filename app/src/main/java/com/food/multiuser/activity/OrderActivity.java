package com.food.multiuser.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.food.multiuser.R;
import com.food.multiuser.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class OrderActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager2;
    private String TAG = "OrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initObjects();
    }

    private void initObjects() {
        tabLayout = findViewById(R.id.toolbar);
        viewPager2 = findViewById(R.id.viewPager);
        getSupportActionBar().setTitle("Order");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager2);
    }


}