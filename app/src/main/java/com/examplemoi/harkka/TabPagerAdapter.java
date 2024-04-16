package com.examplemoi.harkka;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.examplemoi.harkka.fragments.CompareFragment;
import com.examplemoi.harkka.fragments.InfoFragment;
import com.examplemoi.harkka.fragments.QuizFragment;

public class TabPagerAdapter extends FragmentStateAdapter {
    public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new InfoFragment();
            case 1:
                return new CompareFragment();
            case 2:
                return new QuizFragment();
            default:
                return new InfoFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
