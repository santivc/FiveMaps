package com.example.fivemaps;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fivemaps.Fragments.ExplorarFragment;
import com.example.fivemaps.Fragments.IrFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int numTabs) {
        super(fm, numTabs);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ExplorarFragment();
            case 1:
                return new IrFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
