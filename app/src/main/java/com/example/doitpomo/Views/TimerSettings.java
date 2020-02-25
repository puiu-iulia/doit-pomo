package com.example.doitpomo.Views;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.doitpomo.R;
import com.example.doitpomo.Utils.PrefUtils;

public class TimerSettings extends Fragment {

    TextView workTimeStart, workTimeStop, breakTimeStart, breakTimeStop, breakTimeTextView,  longBreakTextView, workSessionsTextView,workTimeTextView;
    SeekBar timerSeekbar, breakSeekbar, longBreakSeekbar, workSessionsSeekbar;
    private int workTime, breakTime, longBreakTime, workSessionsNumber;

    public TimerSettings() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View timerSettingsView = inflater.inflate(R.layout.fragment_timer_settings, container, false);

        workTimeTextView = timerSettingsView.findViewById(R.id.workTimeTextView);
        longBreakSeekbar = timerSettingsView.findViewById(R.id.longBreakSeekbar);
        workSessionsSeekbar = timerSettingsView.findViewById(R.id.workSessionsSeekbar);
        timerSeekbar = timerSettingsView.findViewById(R.id.timerSeekbar);
        breakSeekbar = timerSettingsView.findViewById(R.id.breakSeekbar);
        workTimeStart = timerSettingsView.findViewById(R.id.workTimeStart);
        workTimeStop = timerSettingsView.findViewById(R.id.workTimeStop);
        breakTimeStart = timerSettingsView.findViewById(R.id.breakTimeStart);
        breakTimeStop = timerSettingsView.findViewById(R.id.breakTimeStop);
        breakTimeTextView = timerSettingsView.findViewById(R.id.breakTimeTextView);
        longBreakTextView = timerSettingsView.findViewById(R.id.longBreakTimeTextView);
        workSessionsTextView = timerSettingsView.findViewById(R.id.workSessionsTextView);


        timerSeekbar.setMax(90);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timerSeekbar.setMin(1);
        }
        breakSeekbar.setMax(20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            breakSeekbar.setMin(1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            longBreakSeekbar.setMin(1);
        }
        longBreakSeekbar.setMax(30);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            workSessionsSeekbar.setMin(1);
        }
        workSessionsSeekbar.setMax(10);

        final Context context = getActivity().getApplicationContext();
        workTime = PrefUtils.getWorkTime(context);
        breakTime = PrefUtils.getBreakTime(context);
        longBreakTime = PrefUtils.getLongBreakTime(context);
        workSessionsNumber = PrefUtils.getWorkSessions(context);


        timerSeekbar.setProgress(workTime / 60);
        breakSeekbar.setProgress(breakTime / 60);
        longBreakSeekbar.setProgress(longBreakTime / 60);
        workSessionsSeekbar.setProgress(workSessionsNumber);
        workTimeTextView.setText("Work Time: " + workTime / 60 +" min");
        breakTimeTextView.setText("Break Time: " + breakTime / 60 +" min");
        longBreakTextView.setText("Long Break Time: " + longBreakTime / 60 + " min");
        workSessionsTextView.setText("Work Sessions before Long Break: " + workSessionsNumber);


        timerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    workTime = progress * 60;
                    workTimeTextView.setText("Work Time: " + progress +" min");
                    PrefUtils.setWorkTime(context, workTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        breakSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    breakTime = progress * 60;
                    breakTimeTextView.setText("Break Time: " + progress +" min");
                    PrefUtils.setBreakTime(context, breakTime);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        longBreakSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    longBreakTime = progress * 60;
                    longBreakTextView.setText("Long Break Time: " + progress + " min");
                    PrefUtils.setLongBreakTime(context, longBreakTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        workSessionsSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    workSessionsNumber = progress;
                    workSessionsTextView.setText("Work Sessions before Long Break: " + progress);
                    PrefUtils.setWorkSessions(context, workSessionsNumber);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return timerSettingsView;
    }
}
