package tech.terrastorm.doitpomo.Views;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.terrastorm.doitpomo.Activities.DetailsPomoActivity;
import tech.terrastorm.doitpomo.Data.DatabaseHandler;
import tech.terrastorm.doitpomo.Model.TodoItem;
import com.example.doitpomo.R;
import tech.terrastorm.doitpomo.Sync.TimerBroadcastService;
import tech.terrastorm.doitpomo.Utils.Prefs;

public class PomodoroTimer extends Fragment {

    private TextView timerTextView, pomoSessionsText;

    private Button startButton, pauseButton, playButton, breakButton, stopButton;
    private int workTime, totalWorkOnTask;
    private DatabaseHandler db;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private LinearLayout pomoSessionsLayout;

    public PomodoroTimer() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View timerView = inflater.inflate(R.layout.fragment_pomodoro_timer, container, false);

        startButton = timerView.findViewById(R.id.goButton);
        pauseButton = timerView.findViewById(R.id.pauseButton);
        playButton = timerView.findViewById(R.id.playButton);
        breakButton = timerView.findViewById(R.id.breakButton);
        stopButton = timerView.findViewById(R.id.stopButton);
        timerTextView = timerView.findViewById(R.id.countdownChooseTime);
        pomoSessionsLayout = timerView.findViewById(R.id.pomoSessionsLayout);
        pomoSessionsText = timerView.findViewById(R.id.pomoSessionsText);

        workTime = Prefs.getWorkTime(getContext());

        timerTextView.setText(workTime / 60 + ":00");

        final Context context = getActivity().getApplicationContext();

        Log.d("work sessions", String.valueOf(Prefs.getCurrentWorkSession(context)));

        if (Prefs.getCurrentWorkSession(context) > 0) {
            pomoSessionsLayout.setVisibility(View.VISIBLE);
            pomoSessionsText.setText(" x " + Prefs.getCurrentWorkSession(context));
        } else {
            pomoSessionsLayout.setVisibility(View.INVISIBLE);
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);


                Prefs.setIsWorkModeOn(context, true);
                Prefs.setIsStopped(context, false);
                Prefs.setIsBreakModeOn(context, false);
                Prefs.setIsResumed(context, false);

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

                Prefs.setIsWorkModeOn(context, false);
                Prefs.setIsBreakModeOn(context, true);
                Prefs.setIsResumed(context, false);

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
                Prefs.setIsStopped(context, true);
                getActivity().unregisterReceiver(broadcastReceiver);
                getActivity().stopService(new Intent(context, TimerBroadcastService.class));
                timerTextView.setText((workTime / 60) + ":00");
                if (Prefs.getIsWorkModeOn(context)) {
                    createDialogTime();
                }


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
                if (Prefs.getIsWorkModeOn(context)) {
                    breakButton.setVisibility(View.VISIBLE);
                }
                pauseButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.GONE);

            }
        });

        return timerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    private void createDialogTime() {
        alertDialogBuilder = new AlertDialog.Builder(getContext());

        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.confirmation_dialog, null);

        Button noButton = view.findViewById(R.id.noButton);
        Button yesButton = view.findViewById(R.id.yesButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTotalSpent(workTime - Prefs.getRemainingTime(getContext()));
                Prefs.setCurrentWorkSession(getContext(), 0);
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), DetailsPomoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateTotalSpent(int time) {

        db = new DatabaseHandler(getContext());
        TodoItem todoItem = db.getTodoItem(Prefs.getItemId(getContext()));
        todoItem.setTimeSpent(todoItem.getTimeSpent() + time);
        db.updateTodoItem(todoItem);
        db.close();

    }

    private void updateTimer(Intent intent) {

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
//                Notifications.remindUserTimerFinished(getContext());
                stopTimer();
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private void stopTimer() {

//        totalWorkOnTask = totalWorkOnTask + (workTime - (int) totalWork/1000);
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);
        breakButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);

        timerTextView.setText((workTime / 60) + ":00");
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(new Intent(getContext(), TimerBroadcastService.class));

    }

    private void pauseTimer() {
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.VISIBLE);
        Prefs.setIsResumed(getContext(), false);
        Prefs.setIsStopped(getContext(), true);
//        Log.d("time left when paused", String.valueOf(Prefs.getRemindingTime(getContext()));
        getActivity().stopService(new Intent(getContext(), TimerBroadcastService.class));
    }

    private void resumeTimer() {
        pauseButton.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.GONE);
        Prefs.setIsResumed(getContext(), true);
        Prefs.setIsStopped(getContext(), false);
        getActivity().startService(new Intent(getContext(), TimerBroadcastService.class));
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
    }

    @Override
    public void onPause() {
        super.onPause();
//        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTimer(intent);
        }
    };
}
