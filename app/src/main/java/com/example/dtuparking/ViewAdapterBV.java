package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewAdapterBV extends FragmentStatePagerAdapter {
    public ViewAdapterBV(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragmentBV();
            case 1:
                return new HistoryFragment();

            default:
                return new HomeFragmentBV();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
