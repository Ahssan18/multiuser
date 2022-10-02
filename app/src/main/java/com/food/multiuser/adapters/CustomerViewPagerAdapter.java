package com.food.multiuser.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.food.multiuser.fragments.CustomerPendingOrderFragment;
import com.food.multiuser.fragments.CustomerReceivedOrderFragment;

public class CustomerViewPagerAdapter extends FragmentPagerAdapter {

    public CustomerViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new CustomerPendingOrderFragment();
        } else if (position == 1) {
            fragment = new CustomerReceivedOrderFragment();
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
            title = "Received Orders";
        }
        return title;
    }
}
