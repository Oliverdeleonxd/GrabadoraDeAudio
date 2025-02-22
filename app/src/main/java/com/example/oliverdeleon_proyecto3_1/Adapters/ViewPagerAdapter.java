package com.example.oliverdeleon_proyecto3_1.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> fragTitle = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragments(Fragment frament, String title){
        fragments.add(frament);
        fragTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitle.get(position);
    }
}
