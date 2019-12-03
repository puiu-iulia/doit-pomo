package com.example.doitpomo.UI;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.doitpomo.Views.FinishedTasksListFragment;
import com.example.doitpomo.Views.ToDoListFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0: return new ToDoListFragment();
            case 1: return new FinishedTasksListFragment();
        }


        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "To Do";
            case 1: return "Completed";
        }
        return null;
    }
}
