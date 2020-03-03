package com.example.doitpomo.Sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.doitpomo.Data.DatabaseHandler;
import com.example.doitpomo.Model.TodoItem;
import com.example.doitpomo.Utils.Notifications;
import com.example.doitpomo.Utils.Prefs;

public class TimerBroadcastService extends Service {

    public static final String COUNTDOWN_BROADCAST = "do_it_with_pomo_broadcast";
    private final static String TAG = "BroadcastService";
    private DatabaseHandler db;

    Intent intent = new Intent(COUNTDOWN_BROADCAST);

    CountDownTimer workTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();

//        Log.i(TAG, "Starting timer...");

        int time = 0;
        final Context context = getApplicationContext();

        if (Prefs.getIsResumed(context)) {
            time = Prefs.getRemainingTime(context);
        } else {
            if (Prefs.getIsWorkModeOn(context)) {
                time = Prefs.getWorkTime(context);
            } else if (Prefs.getIsBreakModeOn(context)) {
                if (Prefs.getWorkSessions(context) == Prefs.getCurrentWorkSession(context)) {
                    time = Prefs.getLongBreakTime(context);
                    Prefs.setCurrentWorkSession(context, 0);
                } else {
                    time = Prefs.getBreakTime(context);
                }
            }
        }
//        Log.i(TAG, String.valueOf(time));

        workTimer = new CountDownTimer(time * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                intent.putExtra("countdown", millisUntilFinished);
//                Log.i(TAG, Long.toString(millisUntilFinished));
                sendBroadcast(intent);
                Prefs.setRemainingTime(context, (int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Log.d("TAG", "Send notification");
                if (Prefs.getIsWorkModeOn(context)) {
                    updateTotalSpent(Prefs.getWorkTime(context));
                    Prefs.setCurrentWorkSession(context, Prefs.getCurrentWorkSession(context)+ 1);
                }
                Notifications.remindUserTimerFinished(context);
                stopSelf();
            }
        }.start();


    }

    private void updateTotalSpent(int time) {

        db = new DatabaseHandler(getApplicationContext());

        TodoItem todoItem = db.getTodoItem(Prefs.getItemId(getApplicationContext()));
        Log.d("time spent", String.valueOf(todoItem.getTimeSpent()));

        todoItem.setTimeSpent(todoItem.getTimeSpent() + time);
        db.updateTodoItem(todoItem);
        db.close();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Prefs.getIsStopped(getApplicationContext() )) {
            workTimer.cancel();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
