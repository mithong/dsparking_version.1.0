package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewAdapter extends FragmentStatePagerAdapter {
    public ViewAdapter( @NonNull FragmentManager fm, int behavior ) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem( int position ) {
        // chuyển các trang từ home sang history và ngược lại
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new HistoryFragment();
                // trang mặc định là home
            default:
                return new HomeFragment();
        }
    }

    // số layout liên kết với nhau
    @Override
    public int getCount() {
        return 2;
    }
}
