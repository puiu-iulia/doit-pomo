package com.example.doitpomo.Views;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.doitpomo.R;
import com.example.doitpomo.Sync.TimerBroadcastService;
import com.example.doitpomo.Utils.Notifications;
import com.example.doitpomo.Utils.PrefUtils;

public class PomodoroTimer extends Fragment {

    TextView timerTextView;

    Button startButton, pauseButton, playButton, breakButton, stopButton;
    private int workTime;

    public PomodoroTimer() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View timerView = inflater.inflate(R.layout.fragment_pomodoro_timer, container, false);

        return timerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        startButton = view.findViewById(R.id.goButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        playButton = view.findViewById(R.id.playButton);
        breakButton = view.findViewById(R.id.breakButton);
        stopButton = view.findViewById(R.id.stopButton);
        timerTextView = view.findViewById(R.id.countdownChooseTime);

        workTime = PrefUtils.getWorkTime(getContext());

        timerTextView.setText(workTime / 60 + ":00");

        final Context context = getActivity().getApplicationContext();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);


                PrefUtils.setIsWorkModeOn(context, true);
                PrefUtils.setIsBreakModeOn(context, false);
                PrefUtils.setIsResumed(context, false);

                getActivity().stopService(new Intent(context, TimerBroadcastService.class));
                getActivity().startService(new Intent(context, TimerBroadcastService.class));
                getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
            }
        });

        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopButton.setVisibility(View.VISIBLE);
                breakButton.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.VISIBLE);

                PrefUtils.setIsWorkModeOn(context, false);
                PrefUtils.setIsBreakModeOn(context, true);

                getActivity().stopService(new Intent(context, TimerBroadcastService.class));
                getActivity().startService(new Intent(context, TimerBroadcastService.class));
                getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseButton.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                breakButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                getActivity().stopService(new Intent(context, TimerBroadcastService.class));
//                updateTotalSpent();
                timerTextView.setText((workTime / 60) + ":00");
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
                pauseTimer();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeTimer();
                pauseButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.GONE);
                breakButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public void updateTimer(Intent intent) {

        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            int secondsLeft = (int) (millisUntilFinished / 1000);

            int minutes = secondsLeft / 60;
            int seconds = secondsLeft - (minutes * 60);
            String secondString = Integer.toString(seconds);
            if (seconds <= 9) {
                secondString = "0" + secondString;
            }

            timerTextView.setText(minutes + ":" + secondString);



            if (secondsLeft == 0) {
                Log.d("Stop", "millis is 0");
                Notifications.remindUserTimerFinished(getContext());
                stopTimer();
//                updateTotalSpent();
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    public void stopTimer() {

//        totalWorkOnTask = totalWorkOnTask + (workTime - (int) totalWork/1000);
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);
        breakButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

        timerTextView.setText(String.valueOf(workTime / 60) + ":00");
        getActivity().stopService(new Intent(getContext(), TimerBroadcastService.class));

    }

    public void pauseTimer() {
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.VISIBLE);
        PrefUtils.setIsResumed(getContext(), false);
//        Log.d("time left when paused", String.valueOf(PrefUtils.getRemindingTime(getContext()));
        getActivity().stopService(new Intent(getContext(), TimerBroadcastService.class));
    }

    public void resumeTimer() {
        pauseButton.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.GONE);
        PrefUtils.setIsResumed(getContext(), true);
        getActivity().startService(new Intent(getContext(), TimerBroadcastService.class));
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTimer(intent);
        }
    };
}
