package com.example.individualna;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.individualna.model.Category;

/**
 * Поврзува ViewPager2 со по една CategoryFragment страница за секоја категорија.
 */
public class CategoryPagerAdapter extends FragmentStateAdapter {

    public CategoryPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    @Override
    public int getItemCount() {
        return Category.values().length;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return CategoryFragment.newInstance(position);
    }
}
