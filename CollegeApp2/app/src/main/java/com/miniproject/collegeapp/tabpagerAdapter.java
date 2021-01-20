package com.miniproject.collegeapp;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class tabpagerAdapter extends FragmentPagerAdapter {

    String[] tabarray = new String[]{"On-going", "Expired"};

    public tabpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabarray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OnGoing ongoing1 = new OnGoing();
                return ongoing1;
            case 1:
                Expired expired1 = new Expired();
                return expired1;
        }
        return null;
    }

    @Override
    public int getCount() {

        return 2;
    }
}
