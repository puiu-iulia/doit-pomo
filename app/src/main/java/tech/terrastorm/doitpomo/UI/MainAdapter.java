package tech.terrastorm.doitpomo.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tech.terrastorm.doitpomo.Views.Habits;
import tech.terrastorm.doitpomo.Views.PomodoroTimer;
import tech.terrastorm.doitpomo.Views.Stats;
import tech.terrastorm.doitpomo.Views.Subtasks;
import tech.terrastorm.doitpomo.Views.TimerSettings;
import tech.terrastorm.doitpomo.Views.TodoToday;

public class MainAdapter extends FragmentPagerAdapter {


    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: return new TodoToday();
            case 1: return new Habits();
            case 2: return new Stats();
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
            case 0: return "To Do:";
            case 1: return "Habits";
            case 2: return "Stats";
        }
        return null;
    }
}
