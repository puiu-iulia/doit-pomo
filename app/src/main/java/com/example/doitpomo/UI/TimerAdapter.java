package com.example.doitpomo.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.doitpomo.Views.PomodoroTimer;
import com.example.doitpomo.Views.Subtasks;
import com.example.doitpomo.Views.TimerSettings;

import java.util.Timer;

public class TimerAdapter  extends FragmentPagerAdapter {


    public TimerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: return new PomodoroTimer();
            case 1: return new TimerSettings();
            case 2: return new Subtasks();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Timer";
            case 1: return "Settings";
            case 2: return "Subtasks";
        }
        return null;
    }
}
