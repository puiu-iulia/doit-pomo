package tech.terrastorm.doitpomo.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tech.terrastorm.doitpomo.Views.PomodoroTimer;
import tech.terrastorm.doitpomo.Views.Subtasks;
import tech.terrastorm.doitpomo.Views.TimerSettings;

public class FragmentsAdapter extends FragmentPagerAdapter {


    public FragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: return new PomodoroTimer();
            case 1: return new Subtasks();
            case 2: return new TimerSettings();
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
            case 1: return "Subtasks";
            case 2: return "Settings";
        }
        return null;
    }
}
