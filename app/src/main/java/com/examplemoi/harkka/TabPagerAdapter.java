package com.examplemoi.harkka;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.examplemoi.harkka.fragments.CompareFragment;
import com.examplemoi.harkka.fragments.InfoFragment;
import com.examplemoi.harkka.fragments.QuizFragment;

import java.util.ArrayList;

public class TabPagerAdapter extends FragmentStateAdapter {
    private ArrayList<Info> list = new ArrayList<>();
    public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public Info importInfo(){
        list = DataBuilder.getInstance().getMunicipalities();
        Info info = list.get(0);
        return info;
    }

    public Fragment setInfo(){
        Fragment fragment;
        Bundle data = new Bundle();
        fragment = new InfoFragment();
        data.putSerializable("dataID",importInfo());
        fragment.setArguments(data);
        return fragment;
    }

    public Fragment setCompare(){
        Fragment fragment;
        Bundle data = new Bundle();
        fragment = new CompareFragment();
        data.putSerializable("dataID",importInfo());
        fragment.setArguments(data);
        return fragment;
    }

    public Fragment setQuiz(){
        Fragment fragment;
        Bundle data = new Bundle();
        fragment = new QuizFragment();
        data.putSerializable("dataID",importInfo());
        fragment.setArguments(data);
        return fragment;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return setInfo();
            case 1:
                return setCompare();
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
