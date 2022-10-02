package com.food.multiuser.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.food.multiuser.fragments.OrderHistoryFragment;
import com.food.multiuser.fragments.PendingOrderFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new PendingOrderFragment();
        } else if (position == 1) {
            fragment = new OrderHistoryFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Pending Orders";
        } else if (position == 1) {
            title = "Orders History";
        }
        return title;
    }
}